package utils;

import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class RoleUtils {
    
    // Constants for roles
    public static final int ADMIN_ROLE = 1;
    public static final int EMPLOYEE_ROLE = 2;
    public static final int SHIPPER_ROLE = 3;
    public static final int CLIENT_ROLE = 4;
    
    /**
     * Check session and role, return redirect URL if user is not authorized
     * If user has different role, redirect to their appropriate dashboard
     */
    public static String checkRoleAndRedirect(HttpSession session, RedirectAttributes redirectAttributes, int requiredRole) {
        // Check if session exists and user is logged in
        if (session == null || session.getAttribute("email") == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to access this page.");
            return "redirect:/login.htm";
        }
        
        // Check if role exists in session
        Object roleObj = session.getAttribute("role");
        if (roleObj == null) {
            redirectAttributes.addFlashAttribute("error", "Role not found. Redirecting to home.");
            return "redirect:/";
        }
        
        int currentRole = (Integer) roleObj;
        
        // If user has the required role, allow access
        if (currentRole == requiredRole) {
            return null; // No redirect needed
        }
        
        // If user has different role, redirect to their appropriate dashboard
        String redirectUrl = getRoleDashboard(currentRole);
        if (redirectUrl != null) {
            redirectAttributes.addFlashAttribute("info", "You have been redirected to your dashboard.");
            return redirectUrl;
        }
        
        // Fallback if role is not recognized
        redirectAttributes.addFlashAttribute("error", "You do not have permission to access this page.");
        return "redirect:/";
    }
    
    /**
     * Get the appropriate dashboard URL based on user role
     */
    private static String getRoleDashboard(int role) {
        switch (role) {
            case ADMIN_ROLE:
                return "redirect:/admin/account.htm";
            case EMPLOYEE_ROLE:
                return "redirect:/employee/index.htm";
            case SHIPPER_ROLE:
                return "redirect:/shipper/shippers.htm";
            case CLIENT_ROLE:
                return "redirect:/client/clientboard.htm";
            default:
                return null;
        }
    }
    
    /**
     * Alternative method that allows custom error messages
     */
    public static String checkRoleAndRedirect(HttpSession session, RedirectAttributes redirectAttributes, 
                                            int requiredRole, String customMessage) {
        // Check if session exists and user is logged in
        if (session == null || session.getAttribute("email") == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to access this page.");
            return "redirect:/login.htm";
        }
        
        // Check if role exists in session
        Object roleObj = session.getAttribute("role");
        if (roleObj == null) {
            redirectAttributes.addFlashAttribute("error", "Role not found. Redirecting to home.");
            return "redirect:/";
        }
        
        int currentRole = (Integer) roleObj;
        
        // If user has the required role, allow access
        if (currentRole == requiredRole) {
            return null; // No redirect needed
        }
        
        // If user has different role, redirect to their appropriate dashboard
        String redirectUrl = getRoleDashboard(currentRole);
        if (redirectUrl != null) {
            String message = (customMessage != null && !customMessage.isEmpty()) ? 
                           customMessage : "You have been redirected to your dashboard.";
            redirectAttributes.addFlashAttribute("info", message);
            return redirectUrl;
        }
        
        // Fallback if role is not recognized
        redirectAttributes.addFlashAttribute("error", "You do not have permission to access this page.");
        return "redirect:/";
    }
    
    /**
     * Get role name as string (useful for logging or display)
     */
    public static String getRoleName(int role) {
        switch (role) {
            case ADMIN_ROLE:
                return "Admin";
            case EMPLOYEE_ROLE:
                return "Employee";
            case SHIPPER_ROLE:
                return "Shipper";
            case CLIENT_ROLE:
                return "Client";
            default:
                return "Unknown";
        }
    }
    
    /**
     * Check if current user has required role without redirect
     * Returns true if authorized, false otherwise
     */
    public static boolean hasRequiredRole(HttpSession session, int requiredRole) {
        if (session == null || session.getAttribute("email") == null) {
            return false;
        }
        
        Object roleObj = session.getAttribute("role");
        if (roleObj == null) {
            return false;
        }
        
        int currentRole = (Integer) roleObj;
        return currentRole == requiredRole;
    }
}