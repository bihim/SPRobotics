package com.sproboticworks.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sproboticworks.R;

public class ParsingHtmlActivity extends AppCompatActivity {
    private ImageButton imageButton;
    private TextView textViewToolbar, mainText;
    private String source;
    private String termsAndConditions, privacy, disclaimer, shipping, returnPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parsing_html);

        imageButton = findViewById(R.id.back_button);
        textViewToolbar = findViewById(R.id.toolbarText);
        mainText = findViewById(R.id.mainText);

        imageButton.setOnClickListener(v->{
            onBackPressed();
        });
        setTexts();

        source = getIntent().getStringExtra("source");

        switch (source) {
            case "terms":
                textViewToolbar.setText("Terms and Conditions");
                setTextViewHTML(termsAndConditions);
                break;
            case "privacy":
                textViewToolbar.setText("Privacy Policy");
                setTextViewHTML(privacy);
                break;
            case "disclaimer":
                textViewToolbar.setText("Disclaimer");
                setTextViewHTML(disclaimer);
                break;
            case "shipping":
                textViewToolbar.setText("Shipping");
                setTextViewHTML(shipping);
                break;
            case "returnPolicy":
                textViewToolbar.setText("Return Policy");
                setTextViewHTML(returnPolicy);
                break;
        }
    }

    protected void setTextViewHTML(String html) {
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for (URLSpan span : urls) {
            //makeLinkClickable(strBuilder, span);
        }
        mainText.setText(strBuilder);
        mainText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                // Do something with span.getURL() to handle the link click...
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(span.getURL()));
                startActivity(i);
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    private void setTexts() {
        termsAndConditions = " <div>\n" +
                "                        <p>\n" +
                "                            Welcome to SP Robotic Works Private Limited (“SP Robotic Works”). If you continue to browse and use this website you are agreeing to comply with and be bound by\n" +
                "                            the following terms and conditions of use, which together with our privacy policy govern SP Robotic Works relationship with you in\n" +
                "                            relation to this website.\n" +
                "                        </p>\n" +
                "                        <p>\n" +
                "                            The term SP Robotic Works refers to the owner of\n" +
                "                            the website and the term 'you' refers to\n" +
                "                            the user or viewer of our website.\n" +
                "                        </p>\n" +
                "                        <p>\n" +
                "                            The term “Services” refers to all Products available on the website for viewing and purchase\n" +
                "                            by the user and online portal hosted by SP Robotics to post pictures and videos of projects\n" +
                "                            created under the supervision of SP Robotic Works as well as User\n" +
                "                            Project Uploads ( defined in Para 12 of these Terms and Conditions) .\n" +
                "                        </p>\n" +
                "                        <p>\n" +
                "                            The term “Products” refers to the robotic kits, parts and licenses available for sale on the website of SP Robotic Works\n" +
                "                        </p>\n" +
                "                        <p>\n" +
                "                            The Products sold are for personal use only. Commercial usage of the Products are not allowed without the written consent from SP Robotic Works. You can write to info@sproboticworks.com for request to use the product for commercial purposes.\n" +
                "                        </p>\n" +
                "                        <p>\n" +
                "                            All users who are registered with the Summer Camp 2021, agree to accept the additional terms and conditions mentioned in this Link <a href=\"https://sproboticworks.com/terms-and-conditions-camp-2021\">here.</a>\n" +
                "                        </p>\n" +
                "                        <p>\n" +
                "                            All users who are registered with the Summer Camp 2020, agree to accept the additional terms and conditions mentioned in this Link <a href=\"https://sproboticworks.com/terms-and-conditions-camp\">here.</a>\n" +
                "                        </p>\n" +
                "                        <p>\n" +
                "                            All users who are registered with the National Coding & Robotics Challenge 2020, agree to accept the additional terms and conditions mentioned in this Link <a href=\"https://sproboticworks.com/terms-and-conditions-ncrc\">here.</a>\n" +
                "                        </p>\n" +
                "\n" +
                "                        <p>\n" +
                "                            The use of this website is subject to the following terms of use:\n" +
                "                        </p>\n" +
                "                        <ul class=\"common-li-bottom\">\n" +
                "                            <li>\n" +
                "                                The content of the pages of this website is for your general information and use only. It is subject to change without notice.\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                Neither we nor any third parties provide any warranty or guarantee as to the accuracy, timeliness,\n" +
                "                                performance, completeness or suitability of the information and materials(including the components of the product like batteries, etc)\n" +
                "                                found or offered on this website for any particular purpose.\n" +
                "                                You acknowledge that such information and materials(including the components of the product like batteries, etc)\n" +
                "                                may contain inaccuracies or errors and we expressly exclude liability for any such inaccuracies or errors to the fullest extent permitted by law.\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                Your use of any information or materials (including the components of the product like batteries, etc) on this website is entirely\n" +
                "                                at your own risk, for which we shall not be liable.\n" +
                "                                It shall be your own responsibility to ensure that any products,\n" +
                "                                services or information available through this website meet your specific requirements.\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                This website contains material (including the components of the product like batteries, etc)\n" +
                "                                which is owned by or licensed to us. This material includes, but is not limited to,\n" +
                "                                the design,layout, look, appearance and graphics.\n" +
                "                                Reproduction is prohibited other than in accordance with the copyright notice,\n" +
                "                                which forms part of these terms and conditions\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                All trademarks reproduced in this website which are not the property of, or licensed to, the operator are acknowledged on the website.\n" +
                "                                Reproduction is prohibited other than in accordance with the copyright notice,\n" +
                "                                which forms part of these terms and conditions.\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                Unauthorized use of this website may give rise to a claim for damages and/or be a criminal offence\n" +
                "                            </li>\n" +
                "\n" +
                "                            <li>\n" +
                "                                From time to time this website may also include links to other websites. These links are provided for your convenience to\n" +
                "                                provide further information. They do not signify that we endorse the website(s). We have no responsibility for the content of\n" +
                "                                the linked website(s).\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                You may create a link to this website from another website or document. However, the link to this website cannot be used or created in any manner prohibited under Para 16 of these Terms and Conditions\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                Your use of this website and any dispute arising out of such use of the website is subject to the laws of India or other regulatory\n" +
                "                                authority.\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                By using SP Robotic Works website and service, you agree that you\n" +
                "                                shall be governed by, and construed and interpreted in\n" +
                "                                accordance with, the laws of the Republic of India and\n" +
                "                                shall be subject to the jurisdiction of the courts of\n" +
                "                                Chennai, India.\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                All disputes, actions, claims or controversies\n" +
                "                                (collectively referred to as “Disputes”) relating in\n" +
                "                                any way to your use of any SP Robotic Works service if not\n" +
                "                                settled by the mutual agreement, be referred to arbitration\n" +
                "                                which shall be governed by the Arbitration and Conciliation\n" +
                "                                Act, 1996.\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                All users who are registered with the online portal hosted by SP Robotic Works are allowed to post videos of their projects\n" +
                "                                on the SP Robotic Works website. SP Robotic Works reserves the right to upload the videos and photographs of the projects of the users\n" +
                "                                enrolled in their online courses as part of its promotional activities.\n" +
                "                                Further, the users also reserve the right to upload their projects developed\n" +
                "                                outside the supervision or project scope of SP Robotic Works in the form of videos,\n" +
                "                                pictures and other media on to the social media platforms of SP Robotic Works (“User Project Uploads”) The user reserves the right to mark any User Project Uploads as private and such User Project Uploads cannot be shared by any other user. If the user marks any User Project Uploads as public, then any user can share such User Project Uploads.\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                The user hereby agrees that the SP Robotic Works shall own, and the User agrees to assign and\n" +
                "                                does hereby assign, all rights, title and interest (including but not limited to patent rights, copyrights,\n" +
                "                                trade secret rights, trademark rights, and all other intellectual and industrial property rights of any sort)\n" +
                "                                relating to any and all inventions (whether or not patentable), works of authorship, designs, know-how, ideas and information authored, created, contributed to, made or conceived or reduced to practice, in whole or in part, by the user in such Products/ projects that has been developed by the user under the supervision of SP Robotic Works.\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                Such projects developed by the User under the supervision of SP Robotic once it is uploaded\n" +
                "                                to the website or online platform shall be sole property of SP Robotic Works and SP Robotic Works\n" +
                "                                has the express right to use such projects in the form of pictures or videos or any other media in any platform and that SP Robotic Works may protect the copyright or dispose of or authorize the use of any or all such rights in any manner whatsoever.The user shall have complete right and ownership over the User Project Uploads\n" +
                "                            </li>\n" +
                "\n" +
                "                            <li>\n" +
                "                                SP Robotic Works reserves the right to change the terms, conditions, and notices under which this website is operated and services are offered, without giving any notice of the same\n" +
                "                            </li>\n" +
                "\n" +
                "                            <li>\n" +
                "                                In the event the user posts any inappropriate content with regard to SP Robotics Works\n" +
                "                                or on the official pages and handles of SP Robotic Works on different social media handles\n" +
                "                                or by way of User Project Upload, SP Robotic Works reserves the right to block such user\n" +
                "                                and initiate appropriate legal proceedings against such user.\n" +
                "                                The following shall be considered inappropriate use of the SP Robotic Works website\n" +
                "                                by the user and the user is prohibited from indulging in any of the belowactivities:\n" +
                "                            </li>\n" +
                "                            <li>You consent to receive communications from us by way of e-mails, phone calls and SMS’s with respect to your transactions on our Website. Users will be required to register their valid phone numbers and e-mail addresses to facilitate such communication. We may also use your e-mail address to send You updates, newsletters, changes to features of the Service, and the like to provide You better Services.</li>\n" +
                "                        </ul>\n" +
                "                        <ul class=\"common-li-bottom\" style=\"padding-left: 80px;\">\n" +
                "                            <li>\n" +
                "                                Abuse, harass, threaten, defame, disillusion, erode, abrogate,\n" +
                "                                demean or otherwise violate the legal rights of others;\n" +
                "                            </li>\n" +
                "\n" +
                "                            <li>\n" +
                "                                belongs to another person and to which you not have any right;\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                is grossly harmful, harassing, blasphemous, defamatory, obscene,\n" +
                "                                pornographic, pedophilic, libelous, invasive of another’s privacy,\n" +
                "                                hateful, or racially, ethnically objectionable, disparaging, relating or\n" +
                "                                encouraging money laundering or gambling, or otherwise unlawful in any manner whatever;\n" +
                "                            </li>\n" +
                "\n" +
                "                            <li>\n" +
                "                                harms minors in any way;\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                violates any law for the time being in force;\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                deceives or misleads the addressee about the origin of such messages communicates any information which is grossly offensive or menacing in nature\n" +
                "\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                Engage in any activity that interferes with or disrupts access to the website or its services;\n" +
                "                            </li>\n" +
                "\n" +
                "                            <li>\n" +
                "                                Impersonate any person or entity, or falsely state or otherwise misrepresent your affiliation with a person or entity;\n" +
                "                            </li>\n" +
                "\n" +
                "                            <li>\n" +
                "                                Post any file that infringes the copyright, patent or trademark of other legal entities;\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                Upload or distribute files that contain viruses, corrupted files, or any other similar software or programs that may damage the operation of the website or another's computer;\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                Download any file posted by another user that you know, or reasonably should know, cannot be legally distributed in such manner;\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                Probe, scan or test the vulnerability of the website or any network connected to the website, nor breach the security or authentication measures on the website or any network connected to the website. You may not reverse look-up, trace or seek to trace any information on any other user, of or visitor to, the website, or any other customer of the website, including any website account not owned by you, to its source, or exploit the website or its services or information made available or offered by or through the website, in any way whether or not the purpose is to reveal any information, including but not limited to personal identification information, other than your own information, as provided for by the website;\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                Disrupt or interfere with the security of, or otherwise cause harm to, the website, system resources, accounts, passwords, servers or networks\n" +
                "                                connected to or accessible through the website or any affiliated or linked sites;\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                Collect or store data about other users in\n" +
                "                                connection with the prohibited conduct and activities set forth in this section;\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                Use the website or any material or content for any purpose that is unlawful or prohibited by these Terms, or to solicit the performance of any illegal activity or other activity which infringes the rights of this website or other third parties;\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                Violate any code of conduct or other guidelines, which may be applicable for or to any particular service;\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                Violate the Terms including but not limited to any applicable additional terms of the website contained herein or elsewhere;\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                Violate any code of conduct or other guidelines, which may be applicable for or to any particular service;\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                Threaten the unity, integrity, defense, security or sovereignty of India, friendly relations with foreign states, or public order or cause incitement to the commission of any cognizable offence or prevent investigation of any offence or cause insult to any other nation;\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                Publish, post, disseminate information that is false, inaccurate or misleading; violate any applicable laws or regulations for the time being in force in or outside India;\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                Directly or indirectly, offer, attempt to offer, trade or attempt to trade in any item, the dealing of which is prohibited or restricted in any manner under the provisions of any applicable law, rule, regulation or guideline for the time being in force;\n" +
                "                            </li>\n" +
                "                        </ul>\n" +
                "                    </div>";
        privacy = "<div>\n" +
                "                            <p>\n" +
                "                                This privacy policy sets out how SP Robotic Works (“SPRW”) uses and protects any information that you give us when you use this networking platform (“SPRW Community”) on the website <a href=\"/\" target=\"_blank\">www.sproboticworks.com.</a>\n" +
                "                            </p>\n" +
                "                            <p>\n" +
                "                                This Privacy Policy is intended to explain what data we collect from Users of the website. A “User” means either a member of the SPRW Community” or a non-Member who is simply visiting the Website. Our Service is intended for Users over seven (7) years of age and we will not intentionally collect or maintain information about anyone under seven (7) years of age.\n" +
                "                            </p>\n" +
                "                            <p>\n" +
                "                                SPRW Community provides a social networking platform where learners and other user of the website of SP Robotic Works can interact and follow each other on the platform and share ideas and concepts and participate in social interactions with each other (“Services”). SP Robotic Works is committed to ensuring that your privacy is protected. Should we ask you to provide certain information by which you can be identified when using this website, then you can be assured that it will only be used in accordance with this privacy statement.\n" +
                "                            </p>\n" +
                "                            <p>\n" +
                "                                SPRW may change this policy from time to time by updating this page. You should check this page from time to time to ensure that you are happy with any changes.\n" +
                "                            </p>\n" +
                "                            <p>\n" +
                "                                This privacy policy document outlines the types of personal information received and collected by <a href=\"/\" target=\"_blank\">www.sproboticworks.com</a> (“Website”) and how it is used.\n" +
                "                            </p>\n" +
                "                            <h4 >LOG FILES</h4>\n" +
                "                            <p>\n" +
                "                                We will be collecting non personally identifiable information like many other Web sites, SPRW makes use of log files. The information inside the log files includes internet protocol ( IP ) addresses, type of browser, Internet Service Provider ( ISP ), date/time stamp, referring/exit pages, and number of clicks to analyze trends, administer the site, track users movement around the site, and gather demographic information. IP addresses, and other such information are not linked to any information that is personally identifiable.\n" +
                "                            </p>\n" +
                "                            <h4 >COLLECTION OF YOUR INFORMATION</h4>\n" +
                "                            <p>\n" +
                "                                We may collect the following information:\n" +
                "                            </p>\n" +
                "                            <ul class=\"common-li-bottom\">\n" +
                "                                <li>\n" +
                "                                    Name\n" +
                "                                </li>\n" +
                "                                <li>\n" +
                "                                    Contact information including email address\n" +
                "                                </li>\n" +
                "                                <li>\n" +
                "                                    Demographic information such as gender, postcode, preferences and interests\n" +
                "                                </li>\n" +
                "                                <li>\n" +
                "                                    Profile Photo\n" +
                "                                </li>\n" +
                "                                <li>\n" +
                "                                    Username or Login Ids on other platforms such as Facebook and Google\n" +
                "                                </li>\n" +
                "                            </ul>\n" +
                "                            <p>\n" +
                "                                We require this information to understand your needs and provide you with a better service, and in particular for the following reasons:\n" +
                "                            </p>\n" +
                "                            <ul class=\"common-li-bottom\">\n" +
                "                                <li>\n" +
                "                                    Internal record keeping.\n" +
                "                                </li>\n" +
                "                                <li>\n" +
                "                                    We may use the information to improve our products and services.\n" +
                "                                </li>\n" +
                "                                <li>\n" +
                "                                    We may periodically send promotional emails about new products, special offers or other information which we think you may find interesting using the email address which you have provided.\n" +
                "                                </li>\n" +
                "                                <li>\n" +
                "                                    From time to time, we may also use your information to contact you for market research purposes. We may contact you by email, phone, fax or mail. We may use the information to customize the website according to your interests.\n" +
                "                                </li>\n" +
                "                            </ul>\n" +
                "                            <p>\n" +
                "                                We are committed to ensuring that your information is secure. In order to prevent unauthorized access or disclosure we have put in place suitable physical, electronic and managerial procedures to safeguard and secure the information we collect online.\n" +
                "                            </p>\n" +
                "                            <h4 >USE OF THE INFORMATION</h4>\n" +
                "                            <p>\n" +
                "                                SPRW may share your information with its business partners, third party customers, suppliers or service providers. These business partners, customers, suppliers and service providers shall be contractually obligated to keep your information confidential and secure and they shall be required to use your information only for the needs of the services that are promised or entrusted to them.\n" +
                "                            </p>\n" +
                "                            <p>\n" +
                "                                We shall store and use this information to communicate with you, resolve your concerns, help promote safe use of the Website, collect fees or any other money owed by you, measure consumer interest in the Services provided by us, inform you about online and offline offers, services, and updates, customize your experience, detect and protect us against error, fraud and other criminal activity and/or set in motion the delivery of the products which are ordered by you. We may also use the said information to update our records, maintain your accounts with us, display content such as wish lists and customer reviews and recommend merchandise, updates and services that might be of interest to you. You must note that you are under no obligation to provide such information in the event you do not want to register with us on our Website and avail our offers, recommendations, updates and the like.\n" +
                "                            </p>\n" +
                "                            <p>\n" +
                "                                Subject to the above paragraphs, SPRW shall not sell, rent or provide your information to third parties without your consent, except pursuant to a court order or to comply with applicable law or to protect the rights or interests of SPRW. Further, in order to prevent unauthorized access to or illegal interception of your information by third parties, maintain data accuracy and ensure correct use of information, SPRW shall employ reasonable security practices and procedures and current internet security methods and technologies in compliance with Information Technology Act, 2000 and subordinate legislations framed thereunder.\n" +
                "                            </p>\n" +
                "                            <h4 >DISCLOSURE OF INFORMATION</h4>\n" +
                "                            <p>\n" +
                "                                We may, as directed or approved by you, disclose the information that we collect in the following circumstances:\n" +
                "                            </p>\n" +
                "                            <ul class=\"common-li-bottom\">\n" +
                "                                <li>\n" +
                "                                    To network administrators, and other users authorized by you to access the requested information;\n" +
                "                                </li>\n" +
                "                                <li>\n" +
                "                                    To third-party service providers;\n" +
                "                                </li>\n" +
                "                                <li>\n" +
                "                                    To third-party apps, websites, or other services that you can connect to through the Services;\n" +
                "                                </li>\n" +
                "                                <li>\n" +
                "                                    In connection with a substantial corporate transaction, such as the sale of our Services, a merger, consolidation, asset sale, or in the unlikely event of bankruptcy or insolvency;\n" +
                "                                </li>\n" +
                "                                <li>\n" +
                "                                    To protect the safety of any person; to address fraud, security or technical issues; or to protect Workplace’s rights or property; and\n" +
                "                                </li>\n" +
                "                                <li>\n" +
                "                                    As otherwise directed or authorized by you.\n" +
                "                                </li>\n" +
                "                            </ul>\n" +
                "                            <p>\n" +
                "                                <span style=\"text-decoration: underline;\">Legal Requests:</span> If we receive a subpoena, warrant, discovery order or other request or order from a law enforcement agency, court, other governmental entity, or litigant that seeks data relating to the Services (collectively a “Legal Request”), we will make reasonable attempts to direct the requesting party to seek the data directly from you. If we ask the requesting party to direct the request to you, we will provide your contact information to the requesting party. If legally compelled to produce information and unless legally prohibited, we will use reasonable efforts to notify you. We will direct any requests for information under data protection laws to you unless prohibited by law.\n" +
                "                            </p>\n" +
                "                            <p>\n" +
                "                                <span style=\"text-decoration: underline;\">Aggregate or de-identified data:</span> We may also disclose information that has been aggregated or that otherwise does not personally identify you to third parties and affiliates who may use it for analytics, trends and analysis to improve and provide our products and services.\n" +
                "                            </p>\n" +
                "                            <h4 >COOKIES AND WEB BEACONS</h4>\n" +
                "                            <p>\n" +
                "                                SPRW does use cookies to store information about visitors preferences, record user-specific information on which pages the user access or visit, customize Website page content based on visitors browser type or other information that the visitor sends via their browser.\n" +
                "                            </p>\n" +
                "                            <h4 >ONLINE ADVERTISING WE USE</h4>\n" +
                "                            <p>\n" +
                "                                In addition to using cookies and related technologies as described above, we use third party advertising services, including Google, to help us tailor advertising that we think may be of interest to users and to collect and use other data about user activities on our Website and/or Services (e.g., to allow them to tailor ads on third party services). Third-party vendors, including Google, may deliver ads that might also place cookies and otherwise track user behavior. These companies may use information about user behavior in order to provide customized advertisements across various services and products. In the course of providing these services, products or placing advertisements, these third party companies may place or recognize a unique cookie on your computer, and may record information to these cookies based upon your activities on any of our Sites and/or Services and on third party websites. Each of these companies uses this cookie information according to their own privacy and security policies. If you wish to not have this information used for the purpose of serving you targeted ads, you may opt-out as indicated in this Policy. Please note this does not opt you out of being delivered advertising. You will continue to receive generic ads.\n" +
                "                            </p>\n" +
                "                            <p>\n" +
                "                                You may opt out of Google's use of cookies by visiting Google's Ads Settings or you may opt out of Google Analytics by visiting the Google Analytics opt-out page.\n" +
                "                            </p>\n" +
                "                            <p>\n" +
                "                                Google has additional information available about their Remarketing Privacy Guidelines, and Restrictions.\n" +
                "                            </p>\n" +
                "                            <p>\n" +
                "                                You may view a list of other third party service providers who collect information, and/or opt-out of such collection of information about you, by visiting <a href=\"http://www.networkadvertising.org/choices/\" target=\"_blank\">http://www.networkadvertising.org/choices/</a> or <a href=\"http://www.aboutads.info/choices/\" target=\"_blank\">http://www.aboutads.info/choices/.</a>\n" +
                "                            </p>\n" +
                "                            <h4 >REDIRECTING TO OTHER WEBSITES</h4>\n" +
                "                            <p>\n" +
                "                                Our website may contain links to other websites of interest. However, once you have used these links to leave our site, you should note that we do not have any control over that other website. Therefore, we cannot be responsible for the protection and privacy of any information which you provide whilst visiting such sites and such sites are not governed by this privacy statement. You should exercise caution and look at the privacy statement applicable to the website in question.\n" +
                "                            </p>\n" +
                "                            <h4 >SAFETY AND SECURITY</h4>\n" +
                "                            <p>\n" +
                "                                We use the information that we have to help verify accounts and activity and to promote safety and security on and off our Services on your behalf, such as by investigating suspicious activity or violations of our terms or policies. We work hard to protect Your Account using teams of engineers, automated systems and advanced technology such as encryption and machine learning. For example, we may deploy automated technologies to detect abusive behaviour and content, such as child pornography, that may harm our Services, you, other users, or others.\n" +
                "                            </p>\n" +
                "                            <h4 >HOW SECURE IS MY PERSONAL INFORMATION</h4>\n" +
                "                            <p>\n" +
                "                                The information including the information and pictures which you chose to post on SPRW Community can also be viewed and accessed by other members of the SPRW Community who can view the profiles of the members of the SPRW Community. We use commercially reasonable administrative, technical, personnel and physical measures to safeguard your personal information gathered against loss, theft and unauthorized use, disclosure or modification.  Moroever, SPRW Community also carries features which enable a user to block, unfollow or unfriend another member on the SPRW Community which enables the user to restrict the users who can access his/her profile. However, we cannot completely guarantee the security of any information on the profile. Although we make commercially reasonable efforts to protect it from loss, misuse, or alteration by third parties, you should be aware that there is always some risk involved in transmitting information over the Internet.\n" +
                "                            </p>\n" +
                "                            <p>\n" +
                "                                We don’t collect or retain your personal financial information such as bank account, debit card or credit card data.\n" +
                "                            </p>\n" +
                "                            <h4 >CLOSURE OF AN ACCOUNT OPENED ON THE WEBSITE</h4>\n" +
                "                            <p>\n" +
                "                                If you would like to stop using the Services, you should contact <a href=\"mailto:info@sproboticworks.com\" target=\"_blank\">info@sproboticworks.com</a> and we will suspend Your Account and/or delete any information associated with Your Account. It will take 10 days to complete the process and upon account closure, some information will remain as they are backup copies for a reasonable period of time. Please note, content you create and share on the Website will be owned by you and may remain on the website even upon termination or closure of account.\n" +
                "                            </p>\n" +
                "\n" +
                "                            <h4 >AMENDMENT</h4>\n" +
                "                            <p>\n" +
                "                                SPRW Community reserves the right to amend or update the terms of this Privacy Policy without giving any notice to you. It is advised that you regularly go through our Privacy Policy to make sure you are aware of the changes, if any. Further, SPRW allows you to review, change, update or delete your account information at anytime. SPRW, however, reserves the right to verify and authenticate your identity for the same.\n" +
                "                            </p>\n" +
                "                            <h4 >CONTACT</h4>\n" +
                "                            <p>\n" +
                "                                Should you have any grievance/queries/complaints about the collection/use of your information by SPRW, please feel free to contact Mr.Manikandan at <a href=\"mailto:feedback@sproboticworks.com\" target=\"_blank\">feedback@sproboticworks.com</a> or +91 9940999069 / 1800-121-2135(Toll-free)\n" +
                "                            </p>\n" +
                "                            <h4 >CONSENT</h4>\n" +
                "                            <p>\n" +
                "                                By using the website and/or by providing your information, you signify your assent to this Privacy Policy and further you consent to the collection, storage and use of the information you disclose on the website by SPRW in accordance with this Privacy Policy. You further agree and acknowledge that SPRW shall not be liable for any loss or damage or unauthorized use or disclosure of any information provided by you to SPRW in the event the same happens due to any reason(s) which are out of SPRW’s control or anticipation.\n" +
                "                            </p>\n" +
                "\n" +
                "                            <h4 >OPTING OUT AND UNSUBSCRIBING</h4>\n" +
                "                            <p>\n" +
                "                                1. Reviewing, Correcting and Removing Your Personal Information\n" +
                "                            </p>\n" +
                "                            <p>\n" +
                "                                Upon request, SPRW will provide you with the requisite information about whether we hold any of your Personal Information. If you provide us with your Personal Information, you have the following rights with respect to that information:\n" +
                "                            </p>\n" +
                "                            <ul class=\"common-li-bottom\">\n" +
                "                                <li>\n" +
                "                                    To review the information that you have supplied to us\n" +
                "                                </li>\n" +
                "                                <li>\n" +
                "                                    To request that we correct any errors, outdated information, or omissions in user information that you have supplied to us\n" +
                "                                </li>\n" +
                "                                <li>\n" +
                "                                     To request that your user information not be used to contact you\n" +
                "                                </li>\n" +
                "                                <li>\n" +
                "                                     To request that your user information be removed from any solicitation list that we use\n" +
                "                                </li>\n" +
                "                                <li>\n" +
                "                                    To request that your user information be deleted from our records\n" +
                "                                </li>\n" +
                "                                <li>\n" +
                "                                    To opt out of being solicited by SPRW\n" +
                "                                </li>\n" +
                "                            </ul>\n" +
                "                            <p>\n" +
                "                                To exercise any of these rights, please contact us at <a href=\"mailto:info@sproboticworks.com\" target=\"_blank\">info@sproboticworks.com</a>. We will respond to your request to change, correct, or delete your information within a reasonable timeframe and notify you of the action we have taken.\n" +
                "                            </p>\n" +
                "                            <p>\n" +
                "                                2. To Unsubscribe from Our Communications\n" +
                "                            </p>\n" +
                "                            <p>\n" +
                "                                You may unsubscribe from our marketing communications by clicking on the \"unsubscribe\" link located on the bottom of our e-mails, or by sending us an email at <a href=\"mailto:info@sproboticworks.com\" target=\"_blank\">info@sproboticworks.com</a>. Attention: Privacy. Customers cannot opt out of receiving transactional emails related to their account with us.\n" +
                "                            </p>\n" +
                "                        </div>\n" +
                "\n" +
                "\t\n";
        disclaimer = " <div>\n" +
                "                            <p>\n" +
                "                                The information contained in this website is for general information purposes only.\n" +
                "                                The information is provided by SP Robotic Works and while we endeavor to keep the information up to date\n" +
                "                                and correct, we make no representations or warranties of any kind, express or implied,\n" +
                "                                about the completeness, accuracy, reliability, suitability or availability with respect to the website\n" +
                "                                or the information,products, services, or related graphics contained on the website for any purpose.\n" +
                "                                Any reliance you place on such information is therefore strictly at your own risk\n" +
                "                                In no event will we be liable for any loss or damage including without limitation, indirect or consequential loss or damage,\n" +
                "                                or any loss or damage whatsoever arising from loss of data or profits arising out of,\n" +
                "                                or in connection with, the use of this website.\n" +
                "                                Through this website you are able to link to other websites which are not under the control of SP Robotic Works.\n" +
                "                                We have no control over the nature, content and availability of those sites.\n" +
                "                                The inclusion of any links does not necessarily imply a recommendation or endorse the views expressed within them.\n" +
                "                                Every effort is made to keep the website up and running smoothly.\n" +
                "                                However, SP Robotic Works takes no responsibility for, and will not be liable for,\n" +
                "                                the website being temporarily unavailable due to technical issues beyond our control\n" +
                "\n" +
                "                            </p>\n" +
                "                            <p>\n" +
                "                                ALL INFORMATION, CONTENT, MATERIALS, PRODUCTS AND OTHER SERVICES INCLUDED ON OR OTHERWISE MADE AVAILABLE TO YOU THROUGH OUR SERVICES ARE PROVIDED BY SP ROBOTIC WORKS ON AN \"AS IS\" AND \"AS AVAILABLE\" BASIS, UNLESS OTHERWISE SPECIFIED IN WRITING. SP ROBOTIC WORKS MAKES NO REPRESENTATIONS OR WARRANTIES OF ANY KIND, EXPRESS OR IMPLIED, AS TO THE OPERATION OF THE SP ROBOTIC WORKS SERVICES, OR THE INFORMATION, CONTENT, MATERIALS, PRODUCTS OR OTHER SERVICES INCLUDED ON\n" +
                "                                OR OTHERWISE MADE AVAILABLE TO YOU THROUGH SP ROBOTIC WORKS SERVICES, UNLESS OTHERWISE SPECIFIED IN WRITING. YOU EXPRESSLY AGREE THAT YOUR USE OF SP ROBOTIC WORKS SERVICES IS AT YOUR SOLE RISK.\n" +
                "                            </p>\n" +
                "                            <p>\n" +
                "                                TO THE FULL EXTENT PERMISSIBLE BY APPLICABLE LAW, SP ROBOTIC WORKS DISCLAIMS ALL WARRANTIES, EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. SP ROBOTIC WORKS DOES NOT WARRANT THAT SP ROBOTIC WORKS SERVICES, INFORMATION, CONTENT, MATERIALS, PRODUCTS OR OTHER SERVICES INCLUDED ON OR OTHERWISE MADE AVAILABLE TO YOU THROUGH SP ROBOTIC WORKS SERVICES, SP ROBOTIC WORKS SERVERS OR\n" +
                "                                ELECTRONIC COMMUNICATIONS SENT FROM SP ROBOTIC WORKS ARE FREE OF VIRUSES OR OTHER HARMFUL COMPONENTS. SP ROBOTIC WORKS WILL NOT BE LIABLE FOR ANY DAMAGES OF ANY KIND ARISING FROM THE USE OF ANY SP ROBOTIC WORKS SERVICE, OR FROM ANY INFORMATION, CONTENT, MATERIALS, PRODUCTS OR OTHER SERVICES INCLUDED ON OR OTHERWISE MADE AVAILABLE TO YOU THROUGH ANY SP ROBOTIC WORKS SERVICE, INCLUDING, BUT NOT LIMITED TO DIRECT,\n" +
                "                                INDIRECT, INCIDENTAL, PUNITIVE, AND CONSEQUENTIAL DAMAGES,\n" +
                "                                UNLESS OTHERWISE SPECIFIED IN WRITING.\n" +
                "\n" +
                "                            </p>\n" +
                "\n" +
                "                            <h4 >IMAGES</h4>\n" +
                "                            <p>\n" +
                "                                We do not own and do not claim to own all the images appearing on our website/ Facebook/any Social-Media page. The images belong to their respective owners, who have copyright over them.\n" +
                "                                The images are taken from various different sources.\n" +
                "                                If you feel that any image violates your copyright,\n" +
                "                                please write to admin@sproboticworks.com to have it taken down\n" +
                "                            </p>\n" +
                "\n" +
                "                            <h4 >TERMS OF USE</h4>\n" +
                "                            <p>\n" +
                "                                This product is offered to you conditioned upon your acceptance without modification of the terms, conditions, and notices contained on the website of SP ROBOTICS PRIVATE LIMITED.\n" +
                "                                The product (including the battery) offered by us is sensitive in nature and you are responsible for the proper care and maintenance of the same. We would advise that the product is used under adult supervision and do not take any responsibility for any loss or damage of any kind related to the use of our product or the battery\n" +
                "\n" +
                "                            </p>\n" +
                "                            <h4 >EXCLUSIVE OBLIGATION</h4>\n" +
                "                            <p>\n" +
                "                                This product has been designed for the specific use of applications. This product may not be used for unlawful purposes and that use is expressly prohibited under the terms and conditions of its use. The products cannot be used for any other purpose other than the purpose specifically mentioned in the website of SP ROBOTICS PRIVATE LIMITED\n" +
                "                            </p>\n" +
                "                            <h4 >USE LIMITATION </h4>\n" +
                "                            <p>\n" +
                "                                You may not copy, distribute, transmit, display, perform, reproduce, publish, license, create derivative works from, transfer, or sell, any information, from the SP ROBOTICS PRIVATE LIMITED website or its products.\n" +
                "                            </p>\n" +
                "                            <h4 >NO WARRANTY </h4>\n" +
                "                            <p>\n" +
                "                                This product is not warranted against any manufactured defect from date of purchase.\n" +
                "                            </p>\n" +
                "                            <h4 >GUARANTEES</h4>\n" +
                "                            <p>\n" +
                "                                No guarantees are given or implied to the product efficiency, product performance and production or its improvement.\n" +
                "                            </p>\n" +
                "                            <h4 >LIMITATION OF LIABILITY </h4>\n" +
                "                            <p>\n" +
                "                                In no event shall SP ROBOTICS PRIVATE LIMITED be liable for any direct, indirect, punitive, incidental, special consequential damages, to property or life, whatsoever arising out of or connected with the use or misuse of its products.\n" +
                "                            </p>\n" +
                "\n" +
                "                            <h4 >OTHER STATEMENTS </h4>\n" +
                "                            <p>\n" +
                "                                ORAL OR OTHER WRITTEN STATEMENTS made by SP ROBOTICS PRIVATE LIMITED or its employees and representatives, DO NOT CONSTITUTE WARRANTIES, and shall not be relied upon by buyer, and is not part of the contract for sale or this limited warranty.\n" +
                "                            </p>\n" +
                "                            <h4 >Entire Obligation</h4>\n" +
                "                            <p>\n" +
                "                                The TERM OF USE, WARRANTY AND DISCLAIMER document states the entire obligation of SP ROBOTICS PRIVATE LIMITED with respect to the products.\n" +
                "                                If any part of this disclaimer is determined to be invalid, void, unenforceable or illegal, including, but not limited to the warranty disclaimers and liability disclaimers and liability limitations set forth above, then the invalid or unenforceable provision will be deemed superseded by a valid, enforceable provision that most closely matches the intent of the original provision and the remainder of the agreement shall remain in full force and effects.\n" +
                "                            </p>\n" +
                "                            <h4 >General </h4>\n" +
                "                            <p>\n" +
                "                                This disclaimer statement is governed by the laws of India. You hereby consent to the exclusive jurisdiction and venue of the Courts of Chennai, in all disputes arising out of or relating to the use of this product. Use of this product is unauthorized in any jurisdiction that does not give effect to all provisions of these terms and conditions, including without limitation this paragraph.\n" +
                "                            </p>\n" +
                "                            <h4 >Modification of Terms and Conditions</h4>\n" +
                "                            <p>\n" +
                "                                SP ROBOTICS PRIVATE LIMITED reserves the right to change the terms, conditions, and notices under which their products are offered, without giving any notice of the same.\n" +
                "                            </p>\n" +
                "                            <h4 >GENERAL DISCLAIMER</h4>\n" +
                "                            <p>\n" +
                "                                You assume all responsibility and risk with respect to your use of our website, which is provided “as is” without warranties, representations or conditions of any kind, either express or implied, with regard to information accessed from or via our website, including without limitation, all content and materials, and functions and services provided on our website and our products, all of which are provided without warranty of any kind, including but not limited to warranties concerning the availability, accuracy, completeness or usefulness of content or information, uninterrupted access, and any warranties of title, non-infringement, merchantability or fitness for a particular purpose. We do not warrant that our website or its functioning or the content and material of the services made available thereby will be timely, secure, uninterrupted or error-free, that defects will be corrected, or that our websites or the servers that make our website available are free of viruses or other harmful components. The use of our website is at your sole risk and you assume full responsibility for any costs associated with your use of our website. We will not be liable for any damages of any kind related to the use of our website\n" +
                "\n" +
                "                            </p>\n" +
                "                        </div>";

        shipping = " <div>\n" +
                "                        <p>\n" +
                "                            For domestic buyers, orders are shipped through BlueDart, DTDC or speed post only.\n" +
                "                            Orders will be shipped through DHL, FEDEX or other reliable shippers. Orders will be\n" +
                "                            shipped within 2 working days, if stocks are available or as per the delivery date agreed at the\n" +
                "                            time of order confirmation.\n" +
                "                        </p>\n" +
                "                        <p>\n" +
                "                            SP Robotic Works is not liable for any delay due to custom processing, weather condition or due to any other reason in delivery\n" +
                "                            by the courier company / postal authorities and only guarantees to hand over the consignment to\n" +
                "                            the courier company or postal authorities within 2 working days from the date of the order\n" +
                "                            and payment or as per the delivery date agreed at the time of order confirmation.\n" +
                "                        </p>\n" +
                "                        <p>\n" +
                "                            Delivery of all orders will be to registered address of the buyer as per the information provided at the time of placement of order.\n" +
                "                            SP Robotic Works is in no way responsible for any damage to the order while in transit to the buyer.\n" +
                "                        </p>\n" +
                "                        <p>\n" +
                "                            As per Government of India regulations, batteries cannot be shipped by air.\n" +
                "                            All the products contain batteries and hence will only be transported by land.\n" +
                "\n" +
                "                        </p>\n" +
                "                        <p>\n" +
                "                            Delivery of our services will be confirmed on your mail ID as\n" +
                "                            specified during registration. For any issues in utilizing our\n" +
                "                            services you may contact our helpdesk on 044-33720610.\n" +
                "                        </p>\n" +
                "                        <h4 >IMPORTANT:</h4>\n" +
                "                        <ul>\n" +
                "                            <li>\n" +
                "                                Generally our web shop predicts accurate shipping charges but in\n" +
                "                                case of any problem or errors we may re-calculate shipping\n" +
                "                                charges and will only dispatch such orders after acceptance\n" +
                "                                from you. If you are not accepting the shipping charges, you\n" +
                "                                will get full refund.\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                SP Robotic works is not responsible for any extra charges once the original package has been shipped. If the customer refuses to pay these extra charges, the return shipping and any additional fees will be taken out of the cost of the order,\n" +
                "                                and any remaining funds will be refunded to the customer.\n" +
                "                            </li>\n" +
                "                        </ul>\n" +
                "                        <h4 >SHIPPING AND DELIVERY TO OVERSEAS CUSTOMERS:</h4>\n" +
                "                        <ul>\n" +
                "                            <li>\n" +
                "                                The kits and products shipped to overseas customers i.e. customers based outside the territory of the Republic of India shall not include or contain batteries. Overseas customers can refer to our online portal where we have provided details of alternates to be used to operate the kits and products including specifications of batteries to be purchased to operate the said kits and products.\n" +
                "                            </li>\n" +
                "\n" +
                "                            <li>\n" +
                "                                SP Robotic Works is not liable for any delay due to custom processing, weather condition or due to any other reason in delivery by the courier company / postal authorities and only guarantees to hand over the consignment to the courier company or postal authorities within 2 working days from the date of the order and payment or as per the delivery date agreed at the time of order confirmation.\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                Any product purchased from our stores may attract custom charges as per the rules of your country and SP Robotic Works is not liable for any such payments or refund due to refusal of acceptance of the parcel by the customer.\n" +
                "                            </li>\n" +
                "                        </ul>\n" +
                "                    </div>";

        returnPolicy = "<div>\n" +
                "                        <p>\n" +
                "                            SP Robotic Works believes in helping its customers as far as possible, and has therefore a liberal return and cancellation policy. Under this policy:\n" +
                "                        </p>\n" +
                "                        <p>\n" +
                "                            If there are any damages or missing parts when you receive the\n" +
                "                            order from us, we give 1 week for customers to report to us, and\n" +
                "                            we will replace the damaged products or send you the missing\n" +
                "                            parts within 10 days. However, the case will not be accepted if\n" +
                "                            the products were damaged by inappropriate use or carelessness.\n" +
                "                            To the product need to be exchanged, firstly, please send the\n" +
                "                            photos of damaged products to us. We will estimate the damages\n" +
                "                            then decide the best way to exchange or return the product.\n" +
                "                        </p>\n" +
                "                        <p>\n" +
                "                            We accept no responsibility for improper installation of our products. Electrical polarity must be properly observed in hooking up electrical components.\n" +
                "                        </p>\n" +
                "                        <p>\n" +
                "                            We also accept no responsibility for damages caused during the shipping or transit of the goods.\n" +
                "                        </p>\n" +
                "                        <p>\n" +
                "                            In case you feel that the product received is not as shown on the site or as per your expectations, you must bring it to the notice of our customer service within 24 hours of receiving the product. The Customer Service Team after looking into your complaint will take an appropriate decision.\n" +
                "                        </p>\n" +
                "                        <p>\n" +
                "                            Cancellations and Refund will be considered only if the request is made before we have initiated the process of shipping them. Once the product shipping has been initiated, cancellation and refund is not available.\n" +
                "                        </p>\n" +
                "                        <p>\n" +
                "                            At all times, it shall be the sole discretion of the SP Robotic Works to determine whether the product is deemed to be returned and/or replaced.\n" +
                "                        </p>\n" +
                "</div>";
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}