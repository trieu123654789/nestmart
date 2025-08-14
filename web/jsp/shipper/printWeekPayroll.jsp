<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>NestMart - Weekly Payroll Report</title>
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
            
            .info-section {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 20px;
                margin-bottom: 30px;
            }
            
            .info-box {
                padding: 15px;
                background-color: #f8f9fa;
                border: 1px solid #dee2e6;
                border-radius: 5px;
            }
            
            .info-box h3 {
                margin: 0 0 10px 0;
                font-size: 16px;
                color: #333;
                border-bottom: 1px solid #dee2e6;
                padding-bottom: 5px;
            }
            
            .info-row {
                display: flex;
                justify-content: space-between;
                margin-bottom: 8px;
            }
            
            .info-row:last-child {
                margin-bottom: 0;
            }
            
            .label {
                font-weight: bold;
                color: #555;
            }
            
            .value {
                color: #333;
            }
            
            .salary-breakdown {
                margin: 30px 0;
                padding: 20px;
                background-color: #f0f8f0;
                border: 2px solid #28a745;
                border-radius: 8px;
            }
            
            .salary-breakdown h3 {
                margin: 0 0 20px 0;
                color: #28a745;
                text-align: center;
                font-size: 18px;
            }
            
            .breakdown-table {
                width: 100%;
                border-collapse: collapse;
            }
            
            .breakdown-table td {
                padding: 12px;
                border-bottom: 1px solid #dee2e6;
                font-size: 14px;
            }
            
            .breakdown-table .item-label {
                font-weight: bold;
                color: #555;
            }
            
            .breakdown-table .item-value {
                text-align: right;
                color: #333;
            }
            
            .breakdown-table .total-row td {
                border-top: 2px solid #28a745;
                border-bottom: 2px solid #28a745;
                font-weight: bold;
                font-size: 16px;
                background-color: #e8f5e8;
            }
            
            .performance-section {
                margin: 30px 0;
                padding: 15px;
                background-color: #fff3cd;
                border: 1px solid #ffeaa7;
                border-radius: 5px;
            }
            
            .performance-section h3 {
                margin: 0 0 15px 0;
                color: #856404;
                text-align: center;
            }
            
            .performance-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
                gap: 15px;
            }
            
            .performance-item {
                text-align: center;
                padding: 10px;
                background-color: white;
                border-radius: 5px;
                border: 1px solid #ffeaa7;
            }
            
            .performance-value {
                display: block;
                font-size: 20px;
                font-weight: bold;
                color: #856404;
                margin-bottom: 5px;
            }
            
            .performance-label {
                font-size: 12px;
                color: #6c757d;
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
                font-size: 14px;
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
            <h2>Weekly Payroll Report</h2>
            <p>Generated on: ${reportDate}</p>
        </div>
        
        <c:choose>
            <c:when test="${schedule != null}">
                <div class="info-section">
                    <div class="info-box">
                        <h3>Shipper Information</h3>
                        <div class="info-row">
                            <span class="label">Shipper ID:</span>
                            <span class="value">${shipperID}</span>
                        </div>
                        <div class="info-row">
                            <span class="label">Email:</span>
                            <span class="value">${shipperEmail}</span>
                        </div>
                    </div>
                    
                    <div class="info-box">
                        <h3>Week Information</h3>
                        <div class="info-row">
                            <span class="label">Week Start:</span>
                            <span class="value"><c:out value="${schedule.weekStartDate}"/></span>
                        </div>
                        <div class="info-row">
                            <span class="label">Week End:</span>
                            <span class="value"><c:out value="${schedule.weekEndDate}"/></span>
                        </div>
                        <div class="info-row">
                            <span class="label">Schedule ID:</span>
                            <span class="value">${schedule.weekScheduleID}</span>
                        </div>
                        <div class="info-row">
                            <span class="label">Payment Date:</span>
                            <span class="value">${paymentDate}</span>
                        </div>
                    </div>
                </div>
                
                <div class="performance-section">
                    <h3>Weekly Performance Summary</h3>
                    <div class="performance-grid">
                        <div class="performance-item">
                            <span class="performance-value">${totalWorkingHours}</span>
                            <div class="performance-label">Working Hours</div>
                        </div>
                        <div class="performance-item">
                            <span class="performance-value">${ordersDelivered}</span>
                            <div class="performance-label">Orders Delivered</div>
                        </div>
                        <div class="performance-item">
                            <span class="performance-value">
                                <fmt:formatNumber value="${overtimeHours}" type="number" maxFractionDigits="1" groupingUsed="false"/>
                            </span>
                            <div class="performance-label">Overtime Hours</div>
                        </div>
                        <div class="performance-item">
                            <span class="performance-value">
                                <c:if test="${totalWorkingHours > 0 && ordersDelivered > 0}">
                                    <fmt:formatNumber value="${totalWorkingHours / ordersDelivered}" type="number" maxFractionDigits="1" groupingUsed="false"/>
                                </c:if>
                                <c:if test="${totalWorkingHours == 0 || ordersDelivered == 0}">0.0</c:if>
                            </span>
                            <div class="performance-label">Hours per Delivery</div>
                        </div>
                    </div>
                </div>
                
                <div class="salary-breakdown">
                    <h3>💰 Salary Breakdown</h3>
                    <table class="breakdown-table">
                        <tr>
                            <td class="item-label">Regular Hours:</td>
                            <td class="item-value">${totalWorkingHours - overtimeHours} hours</td>
                        </tr>
                        <tr>
                            <td class="item-label">Basic Salary:</td>
                            <td class="item-value">$<fmt:formatNumber value="${basicSalary}" type="number" maxFractionDigits="2" groupingUsed="true"/></td>
                        </tr>
                        <tr>
                            <td class="item-label">Overtime Hours:</td>
                            <td class="item-value"><fmt:formatNumber value="${overtimeHours}" type="number" maxFractionDigits="1" groupingUsed="false"/> hours</td>
                        </tr>
                        <tr>
                            <td class="item-label">Overtime Pay (1.5x rate):</td>
                            <td class="item-value">$<fmt:formatNumber value="${overtimePay}" type="number" maxFractionDigits="2" groupingUsed="true"/></td>
                        </tr>
                        <tr class="total-row">
                            <td class="item-label">TOTAL WEEKLY SALARY:</td>
                            <td class="item-value">$<fmt:formatNumber value="${totalSalary}" type="number" maxFractionDigits="2" groupingUsed="true"/></td>
                        </tr>
                    </table>
                </div>
                
                <div class="info-section">
                    <div class="info-box">
                        <h3>Calculation Details</h3>
                        <div class="info-row">
                            <span class="label">Standard Rate:</span>
                            <span class="value">$<fmt:formatNumber value="${basicSalary / (totalWorkingHours - overtimeHours)}" type="number" maxFractionDigits="2" groupingUsed="true"/>/hour</span>
                        </div>
                        <div class="info-row">
                            <span class="label">Overtime Rate:</span>
                            <span class="value">$<fmt:formatNumber value="${(basicSalary / (totalWorkingHours - overtimeHours)) * 1.5}" type="number" maxFractionDigits="2" groupingUsed="true"/>/hour</span>
                        </div>
                    </div>
                    
                    <div class="info-box">
                        <h3>Notes</h3>
                        <p style="margin: 0; font-size: 11px; line-height: 1.3;">
                            • Salary calculated based on working hours only<br/>
                            • Overtime paid at 1.5x standard rate<br/>
                            • No additional delivery performance bonuses<br/>
                            • Standard employee compensation structure
                        </p>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div style="text-align: center; padding: 40px; color: #666;">
                    <h3>Week Schedule Not Found</h3>
                    <p>The requested week schedule could not be found or you don't have access to view it.</p>
                    <c:if test="${not empty error}">
                        <p><strong>Error:</strong> ${error}</p>
                    </c:if>
                </div>
            </c:otherwise>
        </c:choose>
        
        <div class="footer">
            <p>This report is generated automatically by NestMart Delivery Management System.</p>
            <p>For questions regarding this payroll report, please contact HR department.</p>
            <p><strong>Confidential:</strong> This document contains sensitive payroll information.</p>
        </div>
        
        <script>
            // Auto-print when page loads (optional)
            // window.onload = function() {
            //     window.print();
            // };
        </script>
    </body>
</html>
