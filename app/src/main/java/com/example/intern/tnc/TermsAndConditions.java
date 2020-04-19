package com.example.intern.tnc;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.R;
import com.example.intern.mainapp.MainApp;

public class TermsAndConditions extends AppCompatActivity {
    private static String tnc = "<p><font size = \"20\">Terms and Conditions</font><br><br></p>\n" +
            "<p><b>1. PRODUCTS AND SERVICES</b><br>\n" +
            "Unless otherwise indicated, this app <b>PS by Prarambh</b> and its contents are the property of Prarambh Retail Private Limited and/or its affiliates/ associates (“ PS By Prarambh”), whose registered office is situated at 307 Third Eye One, C.G. Road, Ahmedabad 380006, Gujarat, India. The App is an online service provided by PS By Prarambh, subject to your compliance with the terms and conditions set forth below. Please read this document carefully before accessing or using the App. By accessing or using the App, you agree to be bound by the terms and conditions set forth below. If you do not wish to be bound by these terms and conditions, you may not access or use the App. The App may modify this contract at any time, and such modifications shall be effective immediately upon posting of the modified contract on the App. You agree to review the contract periodically to be aware of such modifications and your continued access or use of the App shall be deemed your conclusive acceptance of the modified contract. All products, services and information displayed on the App constitute an ‘invitation to offer’. Your order for purchase constitutes your ‘offer’ which offer shall be subject to the terms and conditions as listed here-under. PS By Prarambh reserves the right to accept or reject your offer. When you place an order, you will receive an acknowledgement e- mail confirming receipt of your order. This email will only be an acknowledgement and will not constitute acceptance of your order. A contract between us for the purchase of the goods will not be formed until your payment has been approved by us and we have debited your credit or debit card (“Contract”). The App also provides a marketplace that facilitates the online sale and purchase of products and services offered by various affiliates, registered merchants, manufacturers, vendors, service providers of PS By Prarambh. The purchase of the products and services on the App shall be governed by the Contract.<br><br></p>\n" +
            "\n" +
            "<p><b>2. ELIGIBILITY</b><br>\n" +
            "Use of the App is available only to persons who can enter into legally valid contracts in accordance with the provisions of the Indian Contract Act, 1872. If you are a minor i.e. under the age of 18 years, you may use the App only with the involvement of a parent or guardian.<br><br></p>\n" +
            "\n" +
            "<p><b>3. USE OF THE WEBSITE</b><br>\n" +
            "You understand that, except for information, products or services clearly identified as being supplied by the App/ PS By Prarambh, the App/PS By Prarambh does not operate, control or endorse any information, products or services on the Internet in any way. Except for the App’s identified information, products or services, all information, products and services offered through the site or on the Internet generally are offered by third parties, that are not affiliated with the PS By Prarambh. You also understand that PS By Prarambh cannot and does not guarantee or warrant that files available for downloading through the App will be free of infection or viruses, worms, Trojan horses or other code that manifest contaminating or destructive properties. You are responsible for implementing sufficient procedures and checkpoints to satisfy your particular requirements for accuracy of data input and output, and for maintaining a means external to the App for the reconstruction of any lost data. You assume total responsibility and risk for your use of the App and the internet. The App provides the site and related information and quotas and PS By Prarambh does not make any express or implied warranties, representations or endorsements whatsoever (including without limitation warranties of title or non-infringement, or the implied warranties of merchantability or fitness for a particular purpose) with regard to the service, any merchandise information or service provided through the service or on the internet generally, and PS By Prarambh shall not be liable for any cost or damage arising either directly or indirectly from any such transaction. It is solely your responsibility to evaluate the accuracy, completeness and usefulness of all opinions, advice, services, merchandise and other information provided through the service or on the internet generally. PS By Prarambh does not warrant that the service will be uninterrupted or error-free or that defects in the service will be corrected. You understand further that the pure nature of the internet contains unedited materials some of which are sexually explicit or may be offensive to you. Your access to such materials is at your risk and PS By Prarambh has no control over and accepts no responsibility whatsoever for such materials.<br><br></p>\n" +
            "\n" +
            "<p><b>4. CREDIT CARD DETAILS</b><br>\n" +
            "You agree, understand and confirm that the credit card or debit card details provided by you for availing of services and product(s) on the App will be correct and accurate and you shall not use a credit card or debit card which is not lawfully owned by you. You further agree and undertake to provide the correct and valid credit card or debit card details to PS By Prarambh. We hereby undertake that your credit card or debit card information will not be utilized and shared by PS By Prarambh with any of the third parties unless required for fraud verification or by applicable law. PS By Prarambh will not be liable for any credit card or debit card fraud. The liability for use of a credit card or debit card fraudulently will be on you and the onus to prove otherwise shall be exclusively on you.<br><br></p>\n" +
            "\n" +
            "<p><b>5. YOUR CONDUCT</b><br>\n" +
            "You agree and confirm to the following:<br></p>\n" +
            "\n" +
            "<p><br>a. In the case non-delivery occurs on account of a mistake by you (i.e. wrong name or address or any other wrong information provided) any extra cost incurred by PS By Prarambh for re-delivery shall be claimed from you.\n" +
            "<br>b. You will use the services provided by the App, PS By Prarambh, its affiliates, consultants, vendors, contracted companies for lawful purposes only and comply with all applicable laws and regulations while using the App and while transacting on the App.\n" +
            "<br>c. You will provide authentic and true information at all instances where such information is requested of you. PS By Prarambh reserves the right to confirm and validate the information and other details provided by you at any point of time. If upon confirmation your details are found not to be true (wholly or partly) PS By Prarambh has the right, in its sole discretion, to reject the registration and debar you from using the services at the App, without prior intimation.\n" +
            "<br>d. The addresses at which delivery of the product(s) ordered by you are to be made will be correct and proper in all respects.\n" +
            "<br>e. Before placing an order you will check the product(s) description carefully. By placing an order for a product you agree to be bound by the conditions of the sale included in the product’s description.\n" +
            "<br>f. You will not use the App in any way that causes, or is likely to cause the App or access to it to be interrupted, damaged or impaired in any way.<br><br></p>\n" +
            "<p><b>6. PAYMENTS</b><br>\n" +
            "<br>a. Payments can be made by credit card, debit card, net banking. Upon receiving your order we carry out a standard pre-authorization check on your payment card to ensure there are sufficient funds to fulfil the transaction. Goods will not be dispatched until this pre-authorization check has been completed. Your card will be debited once the order has been accepted.\n" +
            "<br>b. Whilst we try and ensure that all details, descriptions and prices which appear on this App are accurate, errors may occur. If we discover an error in the price of any goods which you have ordered we will inform you of this as soon as possible and give you the option of reconfirming your order at the correct price or cancelling it. If we are unable to contact you we will treat the order as cancelled. If you cancel and you have already paid for the goods, you will receive a full refund. The service may contain typographical errors or other errors or inaccuracies and may not be complete or current. We therefore reserve the right to correct any errors, inaccuracies or omissions and to change or update information at any time without prior notice. We reserve the right to refuse to fill any orders that you may place based on information on the service that may contain errors or inaccuracies, including, without limitation, errors, inaccuracies or out-of- date information regarding pricing, shipping, payment terms, or return policies.<br><br></p>\n" +
            "<p><b>7. SHIPPING POLICY</b><br>\n" +
            "<br>a. PS By Prarambh is not liable for any delay in delivery by the courier company/postal authorities and only guarantees to hand over the consignment to the courier company or postal authorities within 7 (seven) working days from the date of the order and payment or as per the delivery date agreed at the time of order confirmation. Delivery of all orders will be made at the address of the buyer as mentioned at the time of order.\n" +
            "<br>b. PS By Prarambh is in no way responsible for any damage to the order while in transit to the buyer.\n" +
            "<br>c. While we shall strive to ship all items in your order together, this may not always be possible due to product characteristics, or availability.\n" +
            "<br>d. Please note all items (including gifts) will be shipped with an invoice mentioning the price, as per applicable laws.\n" +
            "<br>e. For domestic buyers, orders are shipped through registered domestic courier companies and /or speed post only. Orders are shipped within 15 (fifteen) working days or as per the delivery date agreed at the time of order confirmation and delivering of the shipment subject to the courier company/post office norms.\n" +
            "<br>f. For all products over 2.5 kgs we may need to send it by surface transport with a concomitant increase in delivery time.\n" +
            "<br>g. The approximate shipping and delivery time may vary from destination to destination. PS By Prarambh makes no warranties for a specific delivery date unless so specified in the product description.<br><br></p>\n" +
            "<p><b>8. CANCELLATION AND RETURN</b><br>\n" +
            "<b>I. Cancellation</b><br>\n" +
            "<b><i>a) PS By Prarambh believes in helping its customers as far as possible and has therefore a liberal cancellation policy. Under this policy:</i></b><br><br>\n" +
            "\n" +
            "<br><i>a. Cancellations will be considered only if the request is made within 24 (twenty four) hours of placing an order and if we have not already dispatched the goods.</i>\n" +
            "<br>i) There is no cancellation of orders placed under the ‘Same Day Delivery’ category;\n" +
            "<br>ii) No cancellations are entertained for those products that PS By Prarambh’s marketing team has obtained on special days, like Pongal, Diwali, and Valentine’s Day. These are limited occasion offers and therefore cancellations are not possible.\n" +
            "<br>iii) PS By Prarambh does not accept cancellation requests for perishable items like flowers, eatables etc.; and\n" +
            "<br>iv)PS By Prarambh does not accept cancellation requests for consumables like nutrition and diabetic supplements and tests.\n" +
            "<br><br><b>(b) Cancellation/returns may not be possible for certain products under the following conditions:</b>\n" +
            "\n" +
            "<br>i) Damages due to misuse of product(s)\n" +
            "<br>ii) Incidental damage due to malfunctioning of product(s);\n" +
            "<br>iii) Any consumable item which has been used or installed;\n" +
            "<br>iv) Products with tampered or missing serial/UPC numbers;\n" +
            "<br>v) Any damage/defect which are not covered under the manufacturer or vendor's warranty;\n" +
            "<br>vi) Any product that is returned without all original packaging and accessories, including the box, manufacturer's packaging if any, and all other items originally included with the product(s) delivered;\n" +
            "<br>vii) Any wearable product that has been worn, washed, tags removed, stained from trying on and/or odour, smell of smoke, or show any type of damage etc.; or\n" +
            "<br>viii) For hygiene reasons for example toilet and personal care products.\n" +
            "<br><i>(c) PS By Prarambh has right to determine condition of product(s) for refund ability or ex-changeability</i>\n" +
            "\n" +
            "<br><br><b>II. Return & Refund Policy</b><br>\n" +
            "<br>i) The return and exchange policy for products sold on www.PS By Prarambh.in is 15 days from the date of delivery. For certain specific hygiene and edible products, there are no returns / exchanges.\n" +
            "\n" +
            "<br>ii) There could be certain circumstances beyond our control where you could receive damaged/defective product(s) or a product that is not the same as per your original order. We will replace the product to your satisfaction at no extra cost. In such circumstances, before using the product(s), please get in touch with our Customer Service Team who will guide you on the process for the same at our Customer Service email-  crm@prarambhstore.com\n" +
            "\n" +
            "<br>iii) The return process of the product can be restricted depending on the nature and category of the product(s).\n" +
            "\n" +
            "<br><i>(a) Conditions for return:</i>\n" +
            "\n" +
            "<br>i) Please notify us of the receipt of damaged/defective product(s) within maximum 48 (forty eight) hours of delivery.\n" +
            "<br>ii) Product(s) should be unused.\n" +
            "<br>iii) Product(s) should be returned in their original packaging along with the original price tags, labels and invoices.\n" +
            "<br>iv) It is advised that the return packets should be strongly and adequately packed so that there is no further damage of the product(s) in transit.\n" +
            "<br>v) Specific product(s) being offered by our registered vendors may have a different return policy. Please check the policies and terms at the App at the time of purchase.\n" +
            "<br>vi) Please note that unless the product(s) are faulty, we will be entitled to recover any direct costs of having to recover the product(s) from you. In such an occurrence we may set such costs against the amount re-credited to you in the refund\n" +
            "<br><i>(b) Refunds:</i>\n" +
            "<br><b><i>We will process the refund after receipt of the product by PS By Prarambh, its registered vendor or authorised personnel. Refund will be processed based on the mode of payment of the order.</b></i>\n" +
            "\n" +
            "<br>i) Orders paid by credit card or debit card will be refunded by credit back to the credit card or debit card within 7 (seven) working days of the process being completed;\n" +
            "<br>ii) Orders paid by net banking accounts will be credited back to bank account within 7 (seven) working days of the returns process being completed; and\n" +
            "<br>iii) Refunds will be made in case replacement is not possible.\n" +
            "<br><i>(c) The Return Process</i>\n" +
            "<br><b><i>Please follow the below process for returning your parcel to us</i></b>\n" +
            "\n" +
            "<br>Please mail us at crm@prarambhstore.com giving the order no. and the reason you would like to return it.\n" +
            "<br>We will inform you when the return pickup will be done by our logistics partner.\n" +
            "<br>Re-pack the item in its original packaging with labels and tags still attached.\n" +
            "<br>Cover your address label with the PS By Prarambh address found on your delivery note.\n" +
            "<br>Keep your certificate of couriering safely as you will need this as your proof of return\n" +
            "<br>If you are returning a product (not being a damaged or defective product) for a refund, and not purchasing another product, you may be charged for the original shipping cost and a restocking fee.\n" +
            "\n" +
            "<br>Further Please Note: If a rare product is specially ordered for a customer, the product may have to be ordered on a no-return/ refund basis. This, of course, will be informed to the customer at the time of the purchase on the App.\n" +
            "\n" +
            "<br>(d) The Returns Process for a Damaged / Wrong Item\n" +
            "<br>If you receive a damaged product(s) and would like a refund, please send us a message at crm@prarambhstore.com, advising us of your order number (found in Order History in ‘My Account’), along with the name/product code of the item affected and the details of the problem, so that we may investigate this for you. This must be done within 12 (twelve) Hours of receipt of the product. Please return the item to us following the same procedure as stated in the Returns section We‘ll examine the faulty product and be in touch with information of what refund you are entitled to via email within a reasonable period of time. We can alternatively also offer you a free replacement of the product.<br><br></p>\n" +
            "\n" +
            "<p><br><b>9. TRADEMARKS</b>\n" +
            "The entire contents of the App are protected by copyright and trademark laws. The owner of the copyrights and trademarks are PS By Prarambh, its affiliates or other third party licensors. YOU MAY NOT MODIFY, COPY, REPRODUCE, REPUBLISH, UPLOAD, POST, TRANSMIT, OR DISTRIBUTE, IN ANY MANNER, THE MATERIAL ON THE SITE, INCLUDING TEXT, GRAPHICS, CODE AND/OR SOFTWARE. You may print and download portions of material from the different areas of the App solely for your own non-commercial use provided that you agree not to change or delete any copyright or proprietary notices from the materials. You agree to grant to PS By Prarambh a non- exclusive, royalty-free, worldwide, perpetual license, with the right to sub- license, to reproduce, distribute, transmit, create derivative works of, publicly display and publicly perform any materials and other information (including, without limitation, ideas contained therein for new or improved products and services) you submit to any public areas of the App (such as bulletin boards, forums and newsgroups) or by e-mail to PS By Prarambh by all means and in any media now known or hereafter developed. You also grant to PS By Prarambh right to use your name in connection with the submitted materials and other information as well as in connection with all advertising, marketing and promotional material related thereto. You agree that you shall have no recourse against PS By Prarambh for any alleged or actual infringement or misappropriation of any proprietary right in your communications to PS By Prarambh.\n" +
            "PS By Prarambh, Prarambhstore.com, PS By Prarambh India, https://www.prarambhstore.com and other marks indicated on the App are trademarks of PS By Prarambh in India and other countries. Other PS By Prarambh graphics, logos, page headers, button icons, scripts, and service names are trademarks or trade dress of PS By Prarambh. PS By Prarambh’s trademarks and trade dress may not be used in connection with any product or service that is not PS By Prarambh’s as applicable, in any manner that is likely to cause confusion among users, or in any manner that disparages or discredits PS By Prarambh.\n" +
            "All other trademarks not owned by PS By Prarambh that appear on this App are the property of their respective owners, who may or may not be affiliated with, connected to, or sponsored by PS By Prarambh.\n" +
            "<p><b>10. LINKING</b>\n" +
            "<br>(a) Status of linking policy\n" +
            "\n" +
            "<br>PS By Prarambh welcomes links to this App made in accordance with the terms of this linking policy\n" +
            "<br>By using this App you agree to be bound by the terms and conditions of this linking policy.\n" +
            "<br>(b) Links to this App\n" +
            "\n" +
            "<br>i) Links pointing to this App should not be misleading.\n" +
            "<br>ii) Appropriate link text should be always used.\n" +
            "<br>iii) From time to time the URL structure of this App may be updated, and unless PS By Prarambh agrees in writing otherwise, all links should point to https://www.prarambhstore.com\n" +
            "<br>iv) You must not use the PS By Prarambh logo to link to this App (or otherwise) without PS By Prarambh’s express written permission.\n" +
            "<br>v) You must not link to this App using any inline linking technique.\n" +
            "<br>vi) You must not frame the content of this App or use any similar technology in relation to the content of this App.\n" +
            "<br>(c) Links from this App\n" +
            "\n" +
            "<br>This App includes links to other websites owned and operated by third parties. These links are not endorsements or recommendations and are included here for purely informational purposes. PS By Prarambh has no control over the contents of third party websites, and PS By Prarambh accepts no responsibility for them or for any loss or damage that may arise from your use of them. Any of the trademarks, service marks, collective marks, design rights, personality rights or similar rights that are mentioned, used or cited in the App are the property of their respective owners. Their use here does not imply that you may use them for any other purpose other than for the same or a similar informational use Unless otherwise stated PS By Prarambh sites are neither endorsed nor affiliated.\n" +
            "\n" +
            "<br>(d) Removal of links\n" +
            "\n" +
            "<br>You agree that, should PS By Prarambh request the deletion of a link to our App that is within your control, you will delete the link promptly. If you would like PS By Prarambh to remove a link to your website that is included on this App, please contact PS By Prarambh using the contact details below. Note that unless you have a legal right to demand removal, such removal will be at our discretion.<br><br></p>\n" +
            "\n" +
            "<p><br><b>11. JURISDICTION AND ARBITRATION</b>\n" +
            "This Contract shall be construed in accordance with the applicable laws of India. Subject to the provisions below, the courts at Ahmedabad shall have exclusive jurisdiction in any proceeding arising out of this Contract. Any dispute or differences either in interpretation or otherwise of any terms of this Contract between the parties hereto, shall be referred to an independent arbitrator who will be appointed by PS By Prarambh and its decision shall be final and binding on the parties hereto. The above arbitration shall be in accordance with the Arbitration and Conciliation Act, 1996 as amended from time to time. The arbitration shall be held in Ahmedabad. The high court of judicature at Ahmedabad alone shall have the jurisdiction and the laws of India shall apply.<br><br></p>\n" +
            "\n" +
            "<p><br><b>12. LIMITATION OF LIABILITY</b>\n" +
            "<br>In no event will PS By Prarambh be liable for (i) any incidental, consequential, or indirect damages (including, but not limited to, damages for loss of profits, business interruption, loss of programs or information, and the like) arising out of the use of or inability to use the service, or any information, or transactions provided on the service, or downloaded from the service, or any delay of such information or service. Even if PS By Prarambh or its authorized representatives have been advised of the possibility of such damages, or (ii) any claim attributable to errors, omissions, or other inaccuracies in the service and/or materials or information downloaded through the service. Because some states do not allow the exclusion or limitation of liability for consequential or incidental damages, the above limitation may not apply to you, in such states PS By Prarambh’s liability is limited to the greatest extent permitted by law. PS By Prarambh makes no representations whatsoever about any other web site which you may access through this one or which may link to this Site. When you access a non- PS By Prarambh web site, please understand that it is independent from PS By Prarambh, and that PS By Prarambh has no control over the content on that web site. In addition, a link to PS By Prarambh web site does not mean that PS By Prarambh endorses or accepts any responsibility for the content, or the use, of such web site.<br><br></p>\n" +
            "\n" +
            "<p><br><b>13. TERMINATION</b>\n" +
            "<br>i) PS By Prarambh may suspend or terminate your use of website or any service if it believes, in its sole and absolute discretion that you have breached and of the terms of use.\n" +
            "<br>ii) You shall be liable to pay for the services or product(s) that you have already ordered till the time of termination by PS By Prarambh.\n" +
            "<br>iii) The provisions of paragraphs 2 (Eligibility), 3 (Use of the App), 8 (Cancellation and Return Policy), 9 (Trademarks), 10 (Linking), 11 (Jurisdiction and Arbitration), 12 (Limitation of Liability) 13 (Indemnity), 15 (Miscellaneous) and this Clause 14 (Termination) shall survive any termination of this Contract.</p>\n" +
            "<p><br><b>14. INDEMNITY</b>\n" +
            "<br>i) You agree to indemnify and hold harmless PS By Prarambh, its employees, directors, officers, agents, and their successors and assigns from and against any and all claims, liabilities, damages, losses, costs and expenses including attorney’s fees caused by or arising out of claims based upon your actions or inactions, which may result in any loss or liability to PS By Prarambh or any third party including but not limited to breach of any warranties, representations, undertakings, or any relation to non-fulfillment of any of your any obligations under this Contract arising out of your violation of any applicable laws, regulations including but not limited to intellectual property rights, payment of statutory dues and taxes, claim of libel, defamation, violation of rights of privacy or publicity, loss of service by other subscribers, and infringement of intellectual property or other rights. This clause shall survive the expiry or termination of this Contract.<br><br></p>\n" +
            "\n" +
            "<p><br><b>15. MISCELLANEOUS</b>\n" +
            "<br>i) Any cause of action or claim you may have with respect to the Service must be commenced within [1] year after the claim or cause of action arises or such claim or cause of action is barred. Failure of PS By Prarambh to insist upon or enforce strict performance of any provision of this Contract shall not be construed as a waiver of any provision or right. Neither the course of conduct between the parties nor trade practice shall act to modify any provision of this Contract. PS By Prarambh may assign its rights and duties under this Contract to any party at any time without notice to you. If any provision of these terms and conditions is or becomes invalid, unenforceable or non-binding, you shall remain bound by all other provisions hereof. In such event, such invalid provision shall nonetheless be enforced to the fullest extent permitted by applicable law, and you will at least agree to accept a similar effect as the invalid, unenforceable or non-binding provision, given the contents and purpose of these terms and conditions.<br><br></p>\n" +
            "\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        TextView textView = findViewById(R.id.tv_termsnconditions);
        textView.setText(Html.fromHtml(tnc));
        findViewById(R.id.iv_back_button).setOnClickListener(v->{
            Intent intent = new Intent(this, MainApp.class);
            startActivity(intent);
            finish();
        });
        findViewById(R.id.iv_back_button).setOnClickListener(v->{
            onBackPressed();
        });
    }
}
