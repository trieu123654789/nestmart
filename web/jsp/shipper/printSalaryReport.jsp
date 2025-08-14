<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>NestMart - Shipper Payroll Report</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 20px;
                font-size: 12px;
                line-height: 1.4;
            }
            
            .header {
                text-align: center;
                border-bottom: 2px solid #333;
                padding-bottom: 20px;
                margin-bottom: 30px;
            }
            
            .header h1 {
                margin: 0;
                font-size: 24px;
                color: #333;
            }
            
            .header h2 {
                margin: 5px 0;
                font-size: 18px;
                color: #666;
                font-weight: normal;
            }
            
            .shipper-info {
                margin-bottom: 20px;
                padding: 15px;
                background-color: #f8f9fa;
                border: 1px solid #dee2e6;
            }
            
            .shipper-info h3 {
                margin: 0 0 10px 0;
                font-size: 16px;
            }
            
            .info-row {
                display: flex;
                justify-content: space-between;
                margin-bottom: 5px;
            }
            
            .table {
                width: 100%;
                border-collapse: collapse;
                margin-bottom: 20px;
            }
            
            .table th,
            .table td {
                border: 1px solid #dee2e6;
                padding: 8px;
                text-align: left;
            }
            
            .table th {
                background-color: #e9ecef;
                font-weight: bold;
                text-align: center;
            }
            
            .table td.number {
                text-align: right;
            }
            
            .table td.center {
                text-align: center;
            }
            
            .totals-section {
                margin-top: 30px;
                padding: 20px;
                background-color: #f8f9fa;
                border: 2px solid #28a745;
            }
            
            .totals-section h3 {
                margin: 0 0 15px 0;
                color: #28a745;
                text-align: center;
            }
            
            .total-row {
                display: flex;
                justify-content: space-between;
                margin-bottom: 10px;
                font-size: 14px;
            }
            
            .total-row.grand-total {
                border-top: 2px solid #28a745;
                padding-top: 10px;
                font-weight: bold;
                font-size: 16px;
            }
            
            .footer {
                margin-top: 40px;
                text-align: center;
                font-size: 10px;
                color: #666;
                border-top: 1px solid #dee2e6;
                padding-top: 20px;
            }
            
            @media print {
                body {
                    margin: 0;
                    font-size: 11px;
                }
                
                .no-print {
                    display: none !important;
                }
                
                @page {
                    margin: 1cm;
                }
            }
            
            .print-button {
                position: fixed;
                top: 20px;
                right: 20px;
                padding: 10px 20px;
                background-color: #007bff;
                color: white;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                z-index: 1000;
            }
            
            .print-button:hover {
                background-color: #0056b3;
            }
        </style>
    </head>
    <body>
        <button class="print-button no-print" onclick="window.print()">🖨️ Print Report</button>
        
        <div class="header">
            <h1>NestMart Delivery Services</h1>
            <h2>Shipper Payroll Report</h2>
            <p>Generated on: ${reportDate}</p>
        </div>
        
        <div class="shipper-info">
            <h3>Shipper Information</h3>
            <div class="info-row">
                <strong>Shipper ID:</strong>
                <span>${shipperID}</span>
            </div>
            <div class="info-row">
                <strong>Email:</strong>
                <span>${shipperEmail}</span>
            </div>
            <c:if test="${startMonth != null && startYear != null && endMonth != null && endYear != null}">
                <div class="info-row">
                    <strong>Report Period:</strong>
                    <span>${startMonth}/${startYear} - ${endMonth}/${endYear}</span>
                </div>
            </c:if>
        </div>
        
        <c:choose>
            <c:when test="${not empty salaryHistory}">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Period</th>
                            <th>Working Hours</th>
                            <th>Overtime Hours</th>
                            <th>Basic Salary</th>
                            <th>Overtime Pay</th>
                            <th>Total Salary</th>
                            <th>Payment Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="salary" items="${salaryHistory}">
                            <tr>
                                <td class="center">
                                    <c:out value="${salary.weekStartDate}"/> to<br/>
                                    <c:out value="${salary.weekEndDate}"/>
                                </td>
                                <td class="number">
                                    <fmt:formatNumber value="${salary.totalHoursWorked}" type="number" maxFractionDigits="0" groupingUsed="true"/>
                                </td>
                                <td class="number">
                                    <fmt:formatNumber value="${salary.totalOvertimeHours}" type="number" maxFractionDigits="1" groupingUsed="true"/>
                                </td>
                                <td class="number">
                                    $<fmt:formatNumber value="${salary.totalSalary - salary.totalOvertimeSalary}" type="number" maxFractionDigits="2" groupingUsed="true"/>
                                </td>
                                <td class="number">
                                    $<fmt:formatNumber value="${salary.totalOvertimeSalary}" type="number" maxFractionDigits="2" groupingUsed="true"/>
                                </td>
                                <td class="number">
                                    <strong>$<fmt:formatNumber value="${salary.totalSalary}" type="number" maxFractionDigits="2" groupingUsed="true"/></strong>
                                </td>
                                <td class="center">
                                    <c:choose>
                                        <c:when test="${salary.salaryPaymentDate != null}">
                                            <c:out value="${salary.salaryPaymentDate}"/>
                                        </c:when>
                                        <c:otherwise>Pending</c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                
                <div class="totals-section">
                    <h3>Summary Totals</h3>
                    <div class="total-row">
                        <span>Total Working Hours:</span>
                        <span><fmt:formatNumber value="${grandTotalHours}" type="number" maxFractionDigits="0" groupingUsed="true"/> hours</span>
                    </div>
                    <div class="total-row">
                        <span>Total Overtime Hours:</span>
                        <span><fmt:formatNumber value="${grandTotalOvertimeHours}" type="number" maxFractionDigits="1" groupingUsed="true"/> hours</span>
                    </div>
                    <div class="total-row">
                        <span>Total Basic Salary:</span>
                        <span>$<fmt:formatNumber value="${grandTotalSalary - grandTotalOvertimePay}" type="number" maxFractionDigits="2" groupingUsed="true"/></span>
                    </div>
                    <div class="total-row">
                        <span>Total Overtime Pay:</span>
                        <span>$<fmt:formatNumber value="${grandTotalOvertimePay}" type="number" maxFractionDigits="2" groupingUsed="true"/></span>
                    </div>
                    <div class="total-row grand-total">
                        <span>GRAND TOTAL SALARY:</span>
                        <span>$<fmt:formatNumber value="${grandTotalSalary}" type="number" maxFractionDigits="2" groupingUsed="true"/></span>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div style="text-align: center; padding: 40px; color: #666;">
                    <h3>No Salary Records Found</h3>
                    <p>There are no salary records available for the specified period.</p>
                </div>
            </c:otherwise>
        </c:choose>
        
        <div class="footer">
            <p>This report is generated automatically by NestMart Delivery Management System.</p>
            <p>For questions regarding this payroll report, please contact HR department.</p>
        </div>
        
        <script>
            // Auto-print when page loads (optional)
            // window.onload = function() {
            //     window.print();
            // };
        </script>
    </body>
</html>
