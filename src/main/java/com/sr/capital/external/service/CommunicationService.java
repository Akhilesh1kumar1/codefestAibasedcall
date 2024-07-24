package com.sr.capital.external.service;


import com.sr.capital.config.AppProperties;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.client.KaleyraClient;
import com.sr.capital.external.client.NetCoreClient;
import com.sr.capital.external.client.SinfiniClient;
import com.sr.capital.external.dto.request.CommunicationRequestTemp;
import com.sr.capital.external.dto.request.NetCoreSendEmailRequest;
import com.sr.capital.helpers.enums.CommunicationChannels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommunicationService {

    @Autowired
    private KaleyraClient kaleyraClient;

    @Autowired
    private SinfiniClient sinfiniClient;

    @Autowired
    private NetCoreClient netCoreClient;

    @Autowired
    private AppProperties appProperties;
    private final String INVITATION_LINK = "INVITATION_LINK";
    private final String USER_NAME = "USER_NAME";

    private final String SELLER_NAME = "SELLER_NAME";
    private final String VERIFY_EMAIL_CONTENT = "<!DOCTYPE html><html><head><meta charset=\"utf-8\" /><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" /><meta name=\"format-detection\" content=\"telephone=no, date=no, address=no, email=no, url=no\" /><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" /><meta name=\"color-scheme\" content=\"light dark\" /><meta name=\"supported-color-schemes\" content=\"light dark\" /><meta name=\"description\" content=\"Dear Saahil, your order has been shipped and will reach you by dd/mm/yy.\" /><title>Verify Your Email Address</title><style type=\"text/css\">body {margin: 0;padding: 0;background-color: white;border-radius: 11px;}table {border-spacing: 0;}td {padding: 0;}img {border: 0;}.wrapper {width: 100%;table-layout: fixed;background-color: white;font-family: sans-serif;}</style></head><body><div class=\"wrapper\"><table style=\"background-color: #f7f9ff; width: 100%\"><tr><td style=\"padding: 0 3em\"><table width=\"600px\" style=\"border-spacing: 0; padding-top: 1.5em; margin: 0 auto\"><tr><td style=\"text-align: center; padding: 10px\"><img src=\"https://kr-shipmultichannel.s3-ap-southeast-1.amazonaws.com/upload-file/email-templates/seller/Group_3@2x.png\" width=\"204px\" height=\"57px\" style=\"object-fit: contain\" /></td></tr><tr><td><table style=\"width: 100%\"><tbody><tr style=\"background-color: #e7edff\"><td style=\"padding-left: 20px; padding-right: 50px\"><p style=\" color: #6148dc; letter-spacing: -0.5px; font-size: 20px; font-weight: 500; \">Verify Your Email Address</p></td><td><img src=\"https://kr-shipmultichannel-mum.s3-ap-south-1.amazonaws.com/upload-file/email-templates/seller/Group_713@2x.png\" width=\"94\" height=\"90\" style=\" object-fit: contain; float: right; padding: 30px; \" /></td></tr></tbody></table></td></tr><tr><td><table style=\" background-color: white; width: 100%; color: #000000de; font-size: 16px; \"><tbody><tr><td style=\"padding: 2em 1em 1em 1.25em\"><span style=\"font-weight: 500\">Hi [%USER_NAME%],</span></td></tr><tr><td style=\"text-align: center; padding: 0.5em 1em 2em\"><p style=\"text-align: left; margin-bottom: 2rem\">Click on the button below to confirm your email address and secure your Shiprocket account.</p><a href=[%INVITATION_LINK%] role=\"button\" style=\" text-decoration: none; background-color: #6f57e9; width: 180px; font-size: 14px; padding: 12px; color: white; border: 1px solid #6f57e9b3; border-radius: 8px; \" target=\"_blank\">Confirm Your Account</a></td></tr></tbody></table></td></tr><tr><td><div style=\" background: #e6fff2 0% 0% no-repeat padding-box; border-radius: 8px; font: 400 14px Arial; padding: 20px; height: 9.5rem; border-spacing: 10px; background-image: url('https://sr-cdn-1.shiprocket.in/img/sr/common/email-templates/seller/Group+2141%402x.png'); background-size: 100%; \"><a href=\"https://intercom.help/omuni\" role=\"button\" style=\" padding: 12px 6px; margin-top: 6rem; text-decoration: none; background-color: white; width: 180px; color: #6f57e9; border: 1px solid #6f57e9b3; border-radius: 8px; display: flex; align-items: center; justify-content: center; \"><img src=\"https://sr-cdn-1.shiprocket.in/img/sr/common/email-templates/seller/Path%203078@2x.png\" width=\"16\" height=\"16\" style=\"margin-right: 10px\" /><span> Visit Support Center </span></a></div></td></tr><tr><td style=\" background-color: white; color: #000000de; padding: 20px; \"><div style=\"opacity: 0.67; font-size: 12px; margin-bottom: 24px\">Get answers to all your queries by visiting the ShiprocketSupport Center. We're here to help!</div><div style=\"font-size: 14px; margin-bottom: 0\"><div style=\"margin-bottom: 5px\">Happy shipping!</div><div style=\"font-weight: bold\">Team Capital</div></div></td></tr><tr><td style=\"padding: 20px 5px\"><table style=\"width: 100%\"><tr style=\"font-size: 12px; color: #000000ab; opacity: 0.67\"><td style=\"width: 50%\"></td><td style=\"width: 50%; text-align: end\">Connect with us</td></tr><tr><td style=\"width: 50%\"></td><td style=\"width: 50%\"><div style=\"display: table; margin-left: auto\"><a style=\" display: table-cell; text-decoration: none; text-align: center; vertical-align: middle; padding: 0px 5px 4px 0; \" href=\"https://www.facebook.com/Shiprocket/\"><img width=\"13\" height=\"13\" style=\"object-fit: contain\" src=\"https://kr-shipmultichannel.s3-ap-southeast-1.amazonaws.com/upload-file/email-templates/seller/iconfinder_social-facebook_216078@2x.png\" /></a><a style=\" display: table-cell; text-decoration: none; text-align: center; vertical-align: middle; \" href=\"https://twitter.com/shiprocketindia\"><img width=\"30\" height=\"30\" src=\"https://kr-shipmultichannel.s3-ap-southeast-1.amazonaws.com/upload-file/email-templates/seller/Group_750@2x.png\" /></a><a style=\" display: table-cell; text-decoration: none; text-align: center; vertical-align: middle; \" href=\"https://www.instagram.com/shiprocket.in/\"><img width=\"30\" height=\"30\" src=\"https://kr-shipmultichannel.s3-ap-southeast-1.amazonaws.com/upload-file/email-templates/seller/Group_749@2x.png\" /></a><a style=\" display: table-cell; text-decoration: none; text-align: center; vertical-align: middle; \" href=\"https://in.linkedin.com/company/shiprocket\"><img width=\"30\" height=\"30\" src=\"https://kr-shipmultichannel.s3-ap-southeast-1.amazonaws.com/upload-file/email-templates/seller/Group_748@2x.png\" /></a><a style=\" display: table-cell; text-decoration: none; text-align: center; vertical-align: middle; \" href=\"https://www.youtube.com/c/Shiprocketdotin\"><img width=\"30\" height=\"30\" src=\"https://kr-shipmultichannel.s3-ap-southeast-1.amazonaws.com/upload-file/email-templates/seller/Group_747@2x.png\" /></a></div></td></tr></table><br /><br /></td></tr></table></td></tr><tr><td><div style=\" background-color: #e7edff; padding: 12px 0; font-size: 10px; padding: 20px 50px; \"><p style=\"padding-top: 7px\">Shiprocket is a product of Bigfoot Retail Solutions Pvt. Ltd.<a style=\"color: #15c\" href=\"https://www.google.com/maps/search/416,+Udyog+Vihar+III,+Sector+20,%0D%0A++++++++++++++++++++++++++++Gurugram,+Haryana,+122008,+India?entry=gmail&amp;source=g\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://www.google.com/maps/search/416,%2BUdyog%2BVihar%2BIII,%2BSector%2B20,%250D%250A%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2BGurugram,%2BHaryana,%2B122008,%2BIndia?entry%3Dgmail%26source%3Dg&amp;source=gmail&amp;ust=1710998645016000&amp;usg=AOvVaw3ks9IMc34Fb0VuijuI8LUA\">416, Udyog Vihar III, Sector 20, Gurugram, Haryana, 122008,India</a>. You are receiving this email because you registered onShiprocket with this email address.</p><p>Copyright © [%CURRENT_YEAR%] Shiprocket. All Rights Reserved.</p></div></td></tr></table></div></body></html>";;
    private final String LEAD_REPORT_EMAIL_CONTENT = "<!DOCTYPE html><html><head><meta charset=\"utf-8\" /><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" /><meta name=\"format-detection\" content=\"telephone=no, date=no, address=no, email=no, url=no\" /><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" /><meta name=\"color-scheme\" content=\"light dark\" /><meta name=\"supported-color-schemes\" content=\"light dark\" /><meta name=\"description\" content=\"Dear Saahil, your order has been shipped and will reach you by dd/mm/yy.\" /><title>Capital Lead Report</title><style type=\"text/css\">body {margin: 0;padding: 0;background-color: white;border-radius: 11px;}table {border-spacing: 0;}td {padding: 0;}img {border: 0;}.wrapper {width: 100%;table-layout: fixed;background-color: white;font-family: sans-serif;}</style></head><body><div class=\"wrapper\"><table style=\"background-color: #f7f9ff; width: 100%\"><tr><td style=\"padding: 0 3em\"><table width=\"600px\" style=\"border-spacing: 0; padding-top: 1.5em; margin: 0 auto\"><tr><td style=\"text-align: center; padding: 10px\"><img src=\"https://kr-shipmultichannel.s3-ap-southeast-1.amazonaws.com/upload-file/email-templates/seller/Group_3@2x.png\" width=\"204px\" height=\"57px\" style=\"object-fit: contain\" /></td></tr><tr><td><table style=\"width: 100%\"><tbody><tr style=\"background-color: #e7edff\"><td style=\"padding-left: 20px; padding-right: 50px\"><p style=\" color: #6148dc; letter-spacing: -0.5px; font-size: 20px; font-weight: 500; \">Capital Lead Report</p></td><td><img src=\"https://kr-shipmultichannel-mum.s3-ap-south-1.amazonaws.com/upload-file/email-templates/seller/Group_713@2x.png\" width=\"94\" height=\"90\" style=\" object-fit: contain; float: right; padding: 30px; \" /></td></tr></tbody></table></td></tr><tr><td><table style=\" background-color: white; width: 100%; color: #000000de; font-size: 16px; \"><tbody><tr><td style=\"padding: 2em 1em 1em 1.25em\"><span style=\"font-weight: 500\">Hi,</span></td></tr><tr><td style=\"text-align: center; padding: 0.5em 1em 2em\"><p style=\"text-align: left; margin-bottom: 2rem\">Please find the lead report of the capital product .</p></td></tr></tbody></table></td></tr><tr><td><div style=\" background: #e6fff2 0% 0% no-repeat padding-box; border-radius: 8px; font: 400 14px Arial; padding: 20px; height: 9.5rem; border-spacing: 10px; background-image: url('https://sr-cdn-1.shiprocket.in/img/sr/common/email-templates/seller/Group+2141%402x.png'); background-size: 100%; \"><a href=\"https://intercom.help/omuni\" role=\"button\" style=\" padding: 12px 6px; margin-top: 6rem; text-decoration: none; background-color: white; width: 180px; color: #6f57e9; border: 1px solid #6f57e9b3; border-radius: 8px; display: flex; align-items: center; justify-content: center; \"><img src=\"https://sr-cdn-1.shiprocket.in/img/sr/common/email-templates/seller/Path%203078@2x.png\" width=\"16\" height=\"16\" style=\"margin-right: 10px\" /><span> Visit Support Center </span></a></div></td></tr><tr><td style=\" background-color: white; color: #000000de; padding: 20px; \"><div style=\"opacity: 0.67; font-size: 12px; margin-bottom: 24px\">Get answers to all your queries by visiting the ShiprocketSupport Center. We're here to help!</div><div style=\"font-size: 14px; margin-bottom: 0\"><div style=\"margin-bottom: 5px\">Happy shipping!</div><div style=\"font-weight: bold\">Team Capital</div></div></td></tr><tr><td style=\"padding: 20px 5px\"><table style=\"width: 100%\"><tr style=\"font-size: 12px; color: #000000ab; opacity: 0.67\"><td style=\"width: 50%\"></td><td style=\"width: 50%; text-align: end\">Connect with us</td></tr><tr><td style=\"width: 50%\"></td><td style=\"width: 50%\"><div style=\"display: table; margin-left: auto\"><a style=\" display: table-cell; text-decoration: none; text-align: center; vertical-align: middle; padding: 0px 5px 4px 0; \" href=\"https://www.facebook.com/Shiprocket/\"><img width=\"13\" height=\"13\" style=\"object-fit: contain\" src=\"https://kr-shipmultichannel.s3-ap-southeast-1.amazonaws.com/upload-file/email-templates/seller/iconfinder_social-facebook_216078@2x.png\" /></a><a style=\" display: table-cell; text-decoration: none; text-align: center; vertical-align: middle; \" href=\"https://twitter.com/shiprocketindia\"><img width=\"30\" height=\"30\" src=\"https://kr-shipmultichannel.s3-ap-southeast-1.amazonaws.com/upload-file/email-templates/seller/Group_750@2x.png\" /></a><a style=\" display: table-cell; text-decoration: none; text-align: center; vertical-align: middle; \" href=\"https://www.instagram.com/shiprocket.in/\"><img width=\"30\" height=\"30\" src=\"https://kr-shipmultichannel.s3-ap-southeast-1.amazonaws.com/upload-file/email-templates/seller/Group_749@2x.png\" /></a><a style=\" display: table-cell; text-decoration: none; text-align: center; vertical-align: middle; \" href=\"https://in.linkedin.com/company/shiprocket\"><img width=\"30\" height=\"30\" src=\"https://kr-shipmultichannel.s3-ap-southeast-1.amazonaws.com/upload-file/email-templates/seller/Group_748@2x.png\" /></a><a style=\" display: table-cell; text-decoration: none; text-align: center; vertical-align: middle; \" href=\"https://www.youtube.com/c/Shiprocketdotin\"><img width=\"30\" height=\"30\" src=\"https://kr-shipmultichannel.s3-ap-southeast-1.amazonaws.com/upload-file/email-templates/seller/Group_747@2x.png\" /></a></div></td></tr></table><br /><br /></td></tr></table></td></tr><tr><td><div style=\" background-color: #e7edff; padding: 12px 0; font-size: 10px; padding: 20px 50px; \"><p style=\"padding-top: 7px\">Shiprocket is a product of Bigfoot Retail Solutions Pvt. Ltd.<a style=\"color: #15c\" href=\"https://www.google.com/maps/search/416,+Udyog+Vihar+III,+Sector+20,%0D%0A++++++++++++++++++++++++++++Gurugram,+Haryana,+122008,+India?entry=gmail&amp;source=g\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://www.google.com/maps/search/416,%2BUdyog%2BVihar%2BIII,%2BSector%2B20,%250D%250A%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2BGurugram,%2BHaryana,%2B122008,%2BIndia?entry%3Dgmail%26source%3Dg&amp;source=gmail&amp;ust=1710998645016000&amp;usg=AOvVaw3ks9IMc34Fb0VuijuI8LUA\">416, Udyog Vihar III, Sector 20, Gurugram, Haryana, 122008,India</a>. You are receiving this email because you registered onShiprocket with this email address.</p><p>Copyright © [%CURRENT_YEAR%] Shiprocket. All Rights Reserved.</p></div></td></tr></table></div></body></html>";
    private final String LOAN_REPORT_EMAIL_CONTENT = "<!DOCTYPE html><html><head><meta charset=\"utf-8\" /><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" /><meta name=\"format-detection\" content=\"telephone=no, date=no, address=no, email=no, url=no\" /><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" /><meta name=\"color-scheme\" content=\"light dark\" /><meta name=\"supported-color-schemes\" content=\"light dark\" /><meta name=\"description\" content=\"Dear Saahil, your order has been shipped and will reach you by dd/mm/yy.\" /><title>Capital Loan Report</title><style type=\"text/css\">body {margin: 0;padding: 0;background-color: white;border-radius: 11px;}table {border-spacing: 0;}td {padding: 0;}img {border: 0;}.wrapper {width: 100%;table-layout: fixed;background-color: white;font-family: sans-serif;}</style></head><body><div class=\"wrapper\"><table style=\"background-color: #f7f9ff; width: 100%\"><tr><td style=\"padding: 0 3em\"><table width=\"600px\" style=\"border-spacing: 0; padding-top: 1.5em; margin: 0 auto\"><tr><td style=\"text-align: center; padding: 10px\"><img src=\"https://kr-shipmultichannel.s3-ap-southeast-1.amazonaws.com/upload-file/email-templates/seller/Group_3@2x.png\" width=\"204px\" height=\"57px\" style=\"object-fit: contain\" /></td></tr><tr><td><table style=\"width: 100%\"><tbody><tr style=\"background-color: #e7edff\"><td style=\"padding-left: 20px; padding-right: 50px\"><p style=\" color: #6148dc; letter-spacing: -0.5px; font-size: 20px; font-weight: 500; \">Capital Loan Report</p></td><td><img src=\"https://kr-shipmultichannel-mum.s3-ap-south-1.amazonaws.com/upload-file/email-templates/seller/Group_713@2x.png\" width=\"94\" height=\"90\" style=\" object-fit: contain; float: right; padding: 30px; \" /></td></tr></tbody></table></td></tr><tr><td><table style=\" background-color: white; width: 100%; color: #000000de; font-size: 16px; \"><tbody><tr><td style=\"padding: 2em 1em 1em 1.25em\"><span style=\"font-weight: 500\">Hi,</span></td></tr><tr><td style=\"text-align: center; padding: 0.5em 1em 2em\"><p style=\"text-align: left; margin-bottom: 2rem\">Please find the lead report of the capital product .</p></td></tr></tbody></table></td></tr><tr><td><div style=\" background: #e6fff2 0% 0% no-repeat padding-box; border-radius: 8px; font: 400 14px Arial; padding: 20px; height: 9.5rem; border-spacing: 10px; background-image: url('https://sr-cdn-1.shiprocket.in/img/sr/common/email-templates/seller/Group+2141%402x.png'); background-size: 100%; \"><a href=\"https://intercom.help/omuni\" role=\"button\" style=\" padding: 12px 6px; margin-top: 6rem; text-decoration: none; background-color: white; width: 180px; color: #6f57e9; border: 1px solid #6f57e9b3; border-radius: 8px; display: flex; align-items: center; justify-content: center; \"><img src=\"https://sr-cdn-1.shiprocket.in/img/sr/common/email-templates/seller/Path%203078@2x.png\" width=\"16\" height=\"16\" style=\"margin-right: 10px\" /><span> Visit Support Center </span></a></div></td></tr><tr><td style=\" background-color: white; color: #000000de; padding: 20px; \"><div style=\"opacity: 0.67; font-size: 12px; margin-bottom: 24px\">Get answers to all your queries by visiting the ShiprocketSupport Center. We're here to help!</div><div style=\"font-size: 14px; margin-bottom: 0\"><div style=\"margin-bottom: 5px\">Happy shipping!</div><div style=\"font-weight: bold\">Team Capital</div></div></td></tr><tr><td style=\"padding: 20px 5px\"><table style=\"width: 100%\"><tr style=\"font-size: 12px; color: #000000ab; opacity: 0.67\"><td style=\"width: 50%\"></td><td style=\"width: 50%; text-align: end\">Connect with us</td></tr><tr><td style=\"width: 50%\"></td><td style=\"width: 50%\"><div style=\"display: table; margin-left: auto\"><a style=\" display: table-cell; text-decoration: none; text-align: center; vertical-align: middle; padding: 0px 5px 4px 0; \" href=\"https://www.facebook.com/Shiprocket/\"><img width=\"13\" height=\"13\" style=\"object-fit: contain\" src=\"https://kr-shipmultichannel.s3-ap-southeast-1.amazonaws.com/upload-file/email-templates/seller/iconfinder_social-facebook_216078@2x.png\" /></a><a style=\" display: table-cell; text-decoration: none; text-align: center; vertical-align: middle; \" href=\"https://twitter.com/shiprocketindia\"><img width=\"30\" height=\"30\" src=\"https://kr-shipmultichannel.s3-ap-southeast-1.amazonaws.com/upload-file/email-templates/seller/Group_750@2x.png\" /></a><a style=\" display: table-cell; text-decoration: none; text-align: center; vertical-align: middle; \" href=\"https://www.instagram.com/shiprocket.in/\"><img width=\"30\" height=\"30\" src=\"https://kr-shipmultichannel.s3-ap-southeast-1.amazonaws.com/upload-file/email-templates/seller/Group_749@2x.png\" /></a><a style=\" display: table-cell; text-decoration: none; text-align: center; vertical-align: middle; \" href=\"https://in.linkedin.com/company/shiprocket\"><img width=\"30\" height=\"30\" src=\"https://kr-shipmultichannel.s3-ap-southeast-1.amazonaws.com/upload-file/email-templates/seller/Group_748@2x.png\" /></a><a style=\" display: table-cell; text-decoration: none; text-align: center; vertical-align: middle; \" href=\"https://www.youtube.com/c/Shiprocketdotin\"><img width=\"30\" height=\"30\" src=\"https://kr-shipmultichannel.s3-ap-southeast-1.amazonaws.com/upload-file/email-templates/seller/Group_747@2x.png\" /></a></div></td></tr></table><br /><br /></td></tr></table></td></tr><tr><td><div style=\" background-color: #e7edff; padding: 12px 0; font-size: 10px; padding: 20px 50px; \"><p style=\"padding-top: 7px\">Shiprocket is a product of Bigfoot Retail Solutions Pvt. Ltd.<a style=\"color: #15c\" href=\"https://www.google.com/maps/search/416,+Udyog+Vihar+III,+Sector+20,%0D%0A++++++++++++++++++++++++++++Gurugram,+Haryana,+122008,+India?entry=gmail&amp;source=g\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://www.google.com/maps/search/416,%2BUdyog%2BVihar%2BIII,%2BSector%2B20,%250D%250A%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2B%2BGurugram,%2BHaryana,%2B122008,%2BIndia?entry%3Dgmail%26source%3Dg&amp;source=gmail&amp;ust=1710998645016000&amp;usg=AOvVaw3ks9IMc34Fb0VuijuI8LUA\">416, Udyog Vihar III, Sector 20, Gurugram, Haryana, 122008,India</a>. You are receiving this email because you registered onShiprocket with this email address.</p><p>Copyright © [%CURRENT_YEAR%] Shiprocket. All Rights Reserved.</p></div></td></tr></table></div></body></html>";

    @Async
    public void sendOtpForVerification(CommunicationRequestTemp communicationRequest) {
        try {
            switch (communicationRequest.getChannel()) {
                case SMS -> {
                    if (ObjectUtils.isEmpty(communicationRequest.getSmsCommunicationDto())) {
                        throw new CustomException("Communication Error");
                    }
                    if (appProperties.getSinfiniCommunicationEnabled()) {
                        sinfiniClient.sendSmsNotification(communicationRequest.getSmsCommunicationDto());
                    }
                }
                case WHATSAPP -> {
                    if (ObjectUtils.isEmpty(communicationRequest.getWhatsAppCommunicationDto())) {
                        throw new CustomException("Communication Error");
                    }
                    if (appProperties.getKaleyraCommunicationEnabled()) {
                        kaleyraClient.sendWhatsAppNotification(communicationRequest.getWhatsAppCommunicationDto());
                    }
                }
                case SMS_WHATSAPP -> {
                    if (ObjectUtils.isEmpty(communicationRequest.getSmsCommunicationDto()) || ObjectUtils.isEmpty(communicationRequest.getWhatsAppCommunicationDto())) {
                        throw new CustomException("Communication Error");
                    }
                    if (appProperties.getSinfiniCommunicationEnabled()) {
                        sinfiniClient.sendSmsNotification(communicationRequest.getSmsCommunicationDto());
                    }
                    if (appProperties.getKaleyraCommunicationEnabled()) {
                        kaleyraClient.sendWhatsAppNotification(communicationRequest.getWhatsAppCommunicationDto());
                    }
                }
                default -> {

                }
            }
        } catch (Exception ignored) {

        }

    }

    @Async
    public void sendEmail(NetCoreSendEmailRequest request) {
        try {
            if (appProperties.getNetCoreCommunicationEnabled()) {
                netCoreClient.triggerEmail(request);
            }
        } catch (Exception ignored) {

        }

    }

    public CommunicationRequestTemp getCommunicationRequestForOtpVerificationViaSms(String recipientNo, String otp) {
        String body = otp + " is your one-time password (OTP) for Capital account verification. It is valid for 15 minutes. Please do not share it with anyone.Powered by Shiprocket.";
        return CommunicationRequestTemp.builder()
                .channel(CommunicationChannels.SMS)
                .smsCommunicationDto(
                        CommunicationRequestTemp.SmsCommunicationDTO.builder()
                                .recipientNo(recipientNo)
                                .template(appProperties.getSinfiniSmsTemplateId())
                                .body(body)
                                .build()
                )
                .build();
    }

    public CommunicationRequestTemp getCommunicationRequestForOtpVerificationViaWhatsApp(String recipientNo, List<String> params) {
        return CommunicationRequestTemp.builder()
                .channel(CommunicationChannels.WHATSAPP)
                .whatsAppCommunicationDto(
                        CommunicationRequestTemp.WhatsAppCommunicationDTO.builder()
                                .recipientNo(recipientNo)
                                .template(appProperties.getKaleyraWhatsappOtpTemplateName())
                                .params(params)
                                .build()
                )
                .build();
    }

    public CommunicationRequestTemp getCommunicationRequestForOtpVerificationViaSmsAndWhatsApp(String recipientNo, String otp, List<String> whatsAppParameters) {
        String body = otp + " is your one-time password (OTP) for Capital account verification. It is valid for 15 minutes. Please do not share it with anyone.Powered by Shiprocket.";
        return CommunicationRequestTemp.builder()
                .channel(CommunicationChannels.SMS_WHATSAPP)
                .smsCommunicationDto(
                        CommunicationRequestTemp.SmsCommunicationDTO.builder()
                                .recipientNo(recipientNo)
                                .template(appProperties.getSinfiniSmsTemplateId())
                                .body(body)
                                .build()
                )
                .whatsAppCommunicationDto(
                        CommunicationRequestTemp.WhatsAppCommunicationDTO.builder()
                                .recipientNo(recipientNo)
                                .template(appProperties.getKaleyraWhatsappOtpTemplateName())
                                .params(whatsAppParameters)
                                .build()
                )
                .build();
    }

    public NetCoreSendEmailRequest getCommunicationRequestForEmailVerification(String recipientEmail, String recipientName, String emailVerificationLink) {
        String content = VERIFY_EMAIL_CONTENT;
        Map<String, String> attributes = new HashMap<>();
        attributes.put(INVITATION_LINK, emailVerificationLink);
        attributes.put(USER_NAME, recipientName);
        return NetCoreSendEmailRequest.builder()
                .subject("Welcome to Capital. Email Verification")
                .fromAddressInfo(
                        NetCoreSendEmailRequest.AddressInfo.builder()
                                .email(appProperties.getNetCoreFromEmail())
                                .name(appProperties.getNetCoreFromName())
                                .build()
                )
                .contentInfoList(List.of(
                        NetCoreSendEmailRequest.ContentInfo.builder()
                                .type("html")
                                .value(content)
                                .build()
                ))
                .personalizations(List.of(
                        NetCoreSendEmailRequest.Personalization.builder().attributes(attributes)
                                .toAddressInfo(List.of(
                                        NetCoreSendEmailRequest.AddressInfo.builder()
                                                .email(recipientEmail)
                                                .name(recipientName)
                                                .build()
                                ))
                                .build()
                ))
                .build();
    }


    public NetCoreSendEmailRequest getCommunicationRequestForReport(String recipientEmail, String recipientName, String content,String reportName,String fileName,Boolean isLoanReport) {
        String reportContent = LEAD_REPORT_EMAIL_CONTENT;

        if(isLoanReport){
            reportContent =LOAN_REPORT_EMAIL_CONTENT;
        }
        Map<String, String> attributes = new HashMap<>();
        attributes.put(USER_NAME, recipientName);

        return NetCoreSendEmailRequest.builder()
                .subject(reportName)
                .fromAddressInfo(
                        NetCoreSendEmailRequest.AddressInfo.builder()
                                .email(appProperties.getNetCoreFromEmail())
                                .name(appProperties.getNetCoreFromName())
                                .build()
                )
                .contentInfoList(List.of(
                        NetCoreSendEmailRequest.ContentInfo.builder()
                                .type("html")
                                .value(reportContent)
                                .build()
                )).attachments(List.of(NetCoreSendEmailRequest.AttachmentInfo.builder().name(fileName).content(content).build()))
                .personalizations(List.of(
                        NetCoreSendEmailRequest.Personalization.builder().attributes(attributes)
                                .toAddressInfo(List.of(
                                        NetCoreSendEmailRequest.AddressInfo.builder()
                                                .email(recipientEmail)
                                                .name(recipientName)
                                                .build()
                                )).attachments(List.of(NetCoreSendEmailRequest.AttachmentInfo.builder().name(fileName).content(content).build()))
                                .build()
                ))
                .build();
    }


}
