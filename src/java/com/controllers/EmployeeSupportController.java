package com.controllers;

import com.models.Support;
import com.models.SupportDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utils.RoleUtils;

@Controller
@RequestMapping("/employee")
public class EmployeeSupportController {

    @Autowired
    private SupportDAO supportDAO;

    // Show the support message page for the employee
    @GetMapping("/supportmessage")
    public String showSupportMessagePage(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return redirect;

        Integer employeeId = (Integer) session.getAttribute("accountId");
        List<Support> customersWithMessages = supportDAO.getCustomersWithNewMessages();
        model.addAttribute("customers", customersWithMessages);
        model.addAttribute("employeeId", employeeId);

        return "/employee/supportmessage";
    }

    // Get customer messages as XML
    @GetMapping(value = "/getCustomerMessages", produces = {MediaType.APPLICATION_XML_VALUE + ";charset=UTF-8", MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"})
    @ResponseBody
    public ResponseEntity<String> getCustomerMessages(@RequestParam(required = false) Integer customerID, HttpSession session, RedirectAttributes redirectAttributes) {
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("<response><error>Employee not logged in</error></response>");

        if (customerID == null) {
            return ResponseEntity.badRequest().body("<response><error>Customer ID is required</error></response>");
        }

        try {
            List<Support> messages = supportDAO.getMessagesByCustomerIdAndEmployeeId(customerID, (Integer) session.getAttribute("accountId"));
            StringBuilder xmlResponse = new StringBuilder("<response>");
            if (messages == null || messages.isEmpty()) {
                xmlResponse.append("<message>No messages found for this customer.</message>");
            } else {
                xmlResponse.append("<messages>");
                for (Support m : messages) {
                    xmlResponse.append("<message>")
                            .append("<supportID>").append(m.getSupportID()).append("</supportID>")
                            .append("<customerID>").append(m.getCustomerID()).append("</customerID>")
                            .append("<employeeID>").append(m.getEmployeeID() != null ? m.getEmployeeID() : "null").append("</employeeID>")
                            .append("<messageContent>").append(m.getMessage()).append("</messageContent>")
                            .append("<status>").append(m.getStatus()).append("</status>")
                            .append("<sendDate>").append(m.getSendDate()).append("</sendDate>")
                            .append("<sender>").append(m.getEmployeeID() != null ? "employee" : "customer").append("</sender>")
                            .append("</message>");
                }
                xmlResponse.append("</messages>");
            }
            xmlResponse.append("</response>");
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).header("Content-Type", "application/xml; charset=UTF-8").body(xmlResponse.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("<response><error>An error occurred while processing your request: " + e.getMessage() + "</error></response>");
        }
    }

    // Send a message to a customer
    @PostMapping(value = "/sendMessage", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_HTML_VALUE})
    @ResponseBody
    public String sendMessage(@RequestParam("message") String message,
                              @RequestParam("customerID") Integer customerID,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {

        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return "error: Employee not logged in";

        try {
            Support support = new Support();
            support.setCustomerID(customerID);
            support.setEmployeeID((Integer) session.getAttribute("accountId"));
            support.setMessage(message);
            support.setStatus("Sent");
            support.setSendDate(LocalDateTime.now());
            supportDAO.saveMessage(support);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    // Select a message for processing
    @PostMapping("/selectMessage")
    @ResponseBody
    public String selectMessage(@RequestParam("messageId") Integer messageId, HttpSession session, RedirectAttributes redirectAttributes) {

        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return "error";

        Integer employeeId = (Integer) session.getAttribute("accountId");
        Support message = supportDAO.getMessageById(messageId);
        if (message == null || !"New".equals(message.getStatus())) return "error";

        message.setEmployeeID(employeeId);
        message.setStatus("Processing");
        supportDAO.updateMessage(message);

        List<Support> messages = supportDAO.getMessagesByCustomerIdAndEmployeeId(message.getCustomerID(), employeeId);
        for (Support msg : messages) {
            if (msg.getEmployeeID() == null) {
                msg.setEmployeeID(employeeId);
                msg.setStatus("Processing");
                supportDAO.updateMessage(msg);
            }
        }

        session.setAttribute("selectedMessage", messageId);
        return "success";
    }

    // Complete conversation with customer
    @PostMapping("/completeConversation")
    @ResponseBody
    public ResponseEntity<String> completeConversation(@RequestParam("customerID") Integer customerID, HttpSession session, RedirectAttributes redirectAttributes) {

        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error: Invalid employee or customer ID");

        Integer employeeId = (Integer) session.getAttribute("accountId");
        if (employeeId == null || customerID == null) return ResponseEntity.badRequest().body("error: Invalid employee or customer ID");

        try {
            Support finalMessage = new Support();
            finalMessage.setCustomerID(customerID);
            finalMessage.setEmployeeID(employeeId);
            finalMessage.setMessage("Complete conversation by employee");
            finalMessage.setStatus("Closed");
            finalMessage.setSendDate(LocalDateTime.now());
            supportDAO.saveMessage(finalMessage);

            Thread.sleep(500);

            int deletedCount = supportDAO.deleteMessagesByCustomerIdAndEmployeeId(customerID, employeeId);
            return ResponseEntity.ok(deletedCount > 0 ? "success" : "error: No messages found to delete");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("error: " + e.getClass().getName() + " - " + e.getMessage());
        }
    }

    // Poll messages for customer since lastMessageId
    @GetMapping(value = "/pollMessages", produces = {MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"})
    @ResponseBody
    public ResponseEntity<List<Support>> pollMessages(@RequestParam(value = "customerID", required = false) Integer customerID,
                                                      @RequestParam("lastMessageId") Integer lastMessageId,
                                                      HttpSession session,
                                                      RedirectAttributes redirectAttributes) {

        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        Integer employeeId = (Integer) session.getAttribute("accountId");
        List<Support> messages = (customerID != null)
                ? supportDAO.getMessagesByCustomerIdAndEmployeeId(customerID, employeeId)
                : supportDAO.getNewMessages();

        messages = messages.stream().filter(m -> m.getSupportID() > lastMessageId).collect(Collectors.toList());
        return ResponseEntity.ok().header("Content-Type", "application/json; charset=UTF-8").body(messages);
    }

    // Get updated customer list with messages
    @GetMapping(value = "/getUpdatedCustomerList.htm")
    @ResponseBody
    public String getUpdatedCustomerList(HttpSession session, RedirectAttributes redirectAttributes) {
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return "<customers></customers>";

        List<Support> customers = supportDAO.getCustomersWithNewMessages();
        StringBuilder xml = new StringBuilder("<customers>");
        for (Support c : customers) {
            xml.append("<customer>")
                    .append("<customerID>").append(c.getCustomerID()).append("</customerID>")
                    .append("<fullName>").append(escapeXml(c.getFullName())).append("</fullName>")
                    .append("<phoneNumber>").append(escapeXml(c.getPhoneNumber())).append("</phoneNumber>")
                    .append("<sendDate>").append(c.getSendDate() != null ? escapeXml(c.getSendDate().toString()) : "").append("</sendDate>")
                    .append("</customer>");
        }
        xml.append("</customers>");
        return xml.toString();
    }

    // Escape XML special characters
    private String escapeXml(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&apos;");
    }
}
