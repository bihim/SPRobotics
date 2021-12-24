package com.sproboticworks.view.activity;

import static com.sproboticworks.network.util.Constant.ADD_CART;
import static com.sproboticworks.network.util.Constant.EMAIL_LOGIN;
import static com.sproboticworks.network.util.Constant.EMAIL_OTP;
import static com.sproboticworks.network.util.Constant.GET_CART;
import static com.sproboticworks.network.util.Constant.MOBILE_LOGIN;
import static com.sproboticworks.network.util.Constant.MOBILE_OTP;
import static com.sproboticworks.network.zubaer.Global.API_PLACE_HOLDER;
import static com.sproboticworks.network.zubaer.Global.SHOW_ERROR_TOAST;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.orhanobut.logger.Logger;
import com.sproboticworks.R;
import com.sproboticworks.adapter.FaqAdapter;
import com.sproboticworks.model.CourseDetailsStaticModel;
import com.sproboticworks.model.FaqModel;
import com.sproboticworks.model.ProfileEditModel;
import com.sproboticworks.model.cartrespone.CartResponse;
import com.sproboticworks.model.courseresponse.DataItem;
import com.sproboticworks.model.loginresponse.LogInResponse;
import com.sproboticworks.model.mobileotp.PhoneOtpSentResponse;
import com.sproboticworks.network.util.GsonUtil;
import com.sproboticworks.network.util.ToastUtils;
import com.sproboticworks.preferences.SessionManager;
import com.sproboticworks.util.MethodClass;
import com.sproboticworks.util.NetworkCallActivity;
import com.sproboticworks.view.fragment.CourseDetailsFragment;
import com.sproboticworks.view.fragment.CourseLearnFragment;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseDetailsActivity extends NetworkCallActivity {
    ProgressDialog progressDialog;
    ////Firebase
    String phone_number, firebase_otp, otpFor = "";
    FirebaseAuth auth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    MaterialTextView textview_otp_sent_to;
    MaterialButton gotoEmail;
    String verificationCode;
    LinearLayout ll_preview;
    private TabLayout tabLayout;
    private FrameLayout frameLayout;
    private ImageButton imageButtonBack;
    private TextView course_details_title, course_details_age, course_details_money;
    private MaterialButton materialCardViewEnquire;
    private MaterialButton materialCardViewBuy;
    private BottomSheetDialog bottomSheetDialogForPhone, bottomSheetDialogForOtp, bottomSheetDialogForEmail;
    private DataItem courseDetails;
    private String OTP = "";
    private String mobile = "";
    private Activity activity;
    private TextView textViewCourseDetails;
    private ImageView imageViewCourseDetails;
    private String loginType = "M";
    private String email = "";
    /*What will you learn*/
    private MaterialButton firstItemButton, secondItemButton, thirdItemButton, fourthItemButton;
    private ExpandableLayout firstItemLayout, secondItemLayout, thirdItemLayout, fourthItemLayout;
    private TextView firstItemText, secondItemText, thirdItemText, fourthItemText;

    private TextView faqTextDetails;
    private RecyclerView recyclerView;
    private FaqAdapter faqAdapter;
    private ImageButton call_phone, whatsapp;

    //private ImageView firstItemIcon, secondItemIcon, thirdItemIcon, fourthItemIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        activity = this;
        courseDetails = (DataItem) getIntent().getSerializableExtra("MyClass");
        progressDialog = new ProgressDialog(activity, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("please wait...");
        findViewById();
        setButtonCallBacks();
        initLoginBottomSheet();
        setBottomSheet();
        setTabLayout(savedInstanceState);
        startFirebaseLogin();
        setFaqAdapter();
    }

    private void setButtonCallBacks() {

        call_phone.setOnClickListener(v->{
            //1800-121-2135
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + "1800-121-2135"));
            startActivity(intent);
        });

        whatsapp.setOnClickListener(v->{
            String url = "https://api.whatsapp.com/send?phone="+"+918680001868";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        ll_preview.setOnClickListener(view -> {
            if (courseDetails.getMobile_app_video().contains(".mp4")) {
                startActivity(new Intent(CourseDetailsActivity.this, VideoActivity.class)
                        .putExtra("videourl", courseDetails.getMobile_app_video())
                );
            } else {
                Toast.makeText(this, "No Preview Available!!!", Toast.LENGTH_SHORT).show();
            }
        });

        imageButtonBack.setOnClickListener(v -> {
            onBackPressed();
        });
        materialCardViewEnquire.setOnClickListener(v -> {

            if (!SessionManager.isLoggedIn()) {
                bottomSheetDialogForPhone.show();
            } else {
                startActivity(new Intent(this, EnquiryActivity.class).
                        putExtra("bottomTag", "page_1").
                        putExtra("comingFromCourseDetails", true).
                        putExtra("course", courseDetails.getProductId()).
                        putExtra("courseName", courseDetails.getName())
                );
                overridePendingTransition(0, 0);
                finish();
            }
        });
        /*materialCardViewBuy.setOnClickListener(v -> {

        });*/

        String scratch = getCourseDetails("Learn to Code an online Robot to Dance, solve Mazes and 25+ more fun projects. No Prior Coding Knowledge is required", "Lifetime Access with 1:1 Guidance", "Certification upon completion", "Upto 50+ Projects, no limitation..");
        textViewCourseDetails.setText(Html.fromHtml(scratch));
        setStaticCourseDetailsAndWhatWillYouLearn();
    }

    private void setFaqAdapter() {
        ArrayList<FaqModel> scratch = new ArrayList<>();
        scratch.add(new FaqModel("What are the fun things my child will learn?", "In this course, you get to explore what is a Robot using the magic of Coding vis Scratch Programming! Here you get to learn basics of Robots like understanding different sensors like Color Sensor, Distance Sensor, Bumper Sensor etc and also different aspects of Coding like Conditional Statements, Loops. Math Operators etc. The best part is that you get to make your Virtual Robot literally do anything from a Dancing Robot to a Vacuum Cleaner Robot! How cool is that?"));
        scratch.add(new FaqModel("Is it tough for my child?", "Oh no! These courses have been specially designed for the respective age groups and we have seen average academic performers excel in this practical learning. Your child is sure to do wonders with this kit - Get ready to be amazed!"));
        scratch.add(new FaqModel("Will I get a Certificate after Completing?", "Yes, ofcourse! After you finish all chapters, you will get a certificate. You can also submit a video of your practical project to get a Master Certificate."));
        scratch.add(new FaqModel("Should my child have prior coding knowledge?", "Not at all! No Prior Coding knowledge is needed. The course will be taught from the basics and it will be complete fun for your child."));
        scratch.add(new FaqModel("How is it useful for my child?", "This course has shown clear improvement in a child's Creativity, Focus, Logical thinking and Problem solving skills. But thats not all, Coding is the future and your child will be one-up from the others. Let's ensure your child has all the fun for his/her age but still learns the essential skills for a better today and tomorrow."));
        scratch.add(new FaqModel("Is there a Free Trial?", "There is a Free Trial option here. Your child can code & develop an app in the first few chapters. If you feel your child is interested, and wants to continue learning, coding and developing Apps, you can purchase the entire course."));
        scratch.add(new FaqModel("What if there are doubts while learning? How to clarify?", "There is 1:1 Personlized Guidance with the Product Support team through text, video and screenshare support from our tech experts available from 10am to 8pm (Monday Holiday). You can clarify all your doubts here"));
        scratch.add(new FaqModel("Why Scratch Programming Language?", "Scratch is the best and popular programming Language easily understandable by children of ages 7+. To help your child understand even better our Coding platform offers to experiment their output using a Virtual Robot to see how they have coded! Concepts related to Navigation,Sensors, Physics etc. will be learned as and when they progress, discovering true learning while they have fun doing real time experiments such as Maze Solver, Dancing Robot etc."));
        scratch.add(new FaqModel("What is the syllabus for this course?", "Introduction to Robots & Scratch Programming\n" +
                "\n" +
                "Maze Solver Robot using Robot Movements & Navigation\n" +
                "\n" +
                "Coding Your Dancing Robot using Finite Loops\n" +
                "\n" +
                "Vacuum Cleaner Robot using Conditional statements\n" +
                "\n" +
                "Coding your Robot Army using Sensor based applications\n" +
                "\n" +
                "Robotic Calculator using Math Operations in Coding"));
        scratch.add(new FaqModel("What are the things required to learn this course?", "This course does NOT NEED any kit/hardware. It’s completely online and the platform is completely web-based and no software downloads are required. A desktop computer or Laptop or Tablet (both Android/IPad) with a good Internet connection is required to learn the course and to use the coding platform. Please don't forget to use Google Chrome browser to learn Online.\n" +
                "\n" +
                "For Android Tablet - Supported OS Version: Android 6.0+\n" +
                "\n" +
                "For IPad - Supported OS Version: iOS12 or higher "));
        scratch.add(new FaqModel("What can I make after I finish this course?", "Coding is a life skill and you have merely started off with a drop in the ocean! There are so many experiments that you can still do using the Virtual Robot like building Self-Driving Cars, a Delivery Robot, an Obstacle avoider robot, Parking assistant robot and 25+ more such projects!!"));
        scratch.add(new FaqModel("What are the game apps we can build with this online course?", "Scratch Programming examples are so many like Voice based games, text games and game apps where you can play shooting. Imagination is the only limitation."));
        scratch.add(new FaqModel("What software is used for teaching Scratch Programming Online? ", "The course is taught using MIT App Inventor Software. Nothing to worry, we will provide the software and tutorial on how to download during the course."));
        scratch.add(new FaqModel("How to do programming in scratch?", "This course teaches your child from the basics on how to do programming in scratch language. Various experiments and interesting projects like games etc will be done during the course to understand all concepts in depth and help develop your child’s logical thinking and programming skills."));

        ArrayList<FaqModel> android = new ArrayList<>();
        android.add(new FaqModel("What are the fun things my child will learn?", "Every child loves mobile or video games. This course will be equally fun - with lots of learning. They will be building their own video game apps and useful apps like QR Code Apps, GPS Apps too . While they code and have fun, learning happens automatically."));
        android.add(new FaqModel("Is it tough for my child?", "Oh no! These courses have been specially designed for the respective age groups and we have seen average academic performers excel in this practical learning. Your child is sure to do wonders with this kit - Get ready to be amazed!"));
        android.add(new FaqModel("Should my child have prior coding knowledge?", "Not at all! No Coding or Electronics knowledge is needed. The course will be taught from the basics and it will be complete fun for your child."));
        android.add(new FaqModel("How is it useful for my child?", "This course has shown clear improvement in a child's creativity, focus, logical thinking and problem solving skills. But thats not all, Coding and App Building is the future and your child will be one-up from the others. Let's ensure your child has all the fun for his/her age but still learns the essential skills for a better today and tomorrow."));
        android.add(new FaqModel("What are the items required to learn this Online?", " Your child will need a laptop to code and upload into an Android phone. Softwares and installation procedures will be given by us"));
        android.add(new FaqModel("Is there a Free Trial?", "There is a Free Trial option here. Your child can code & develop an app in the first few chapters. If you feel your child is interested, and wants to continue learning, coding and developing Apps, you can purchase the entire course."));
        android.add(new FaqModel("What if there are doubts while learning? How to clarify?", "There is 1:1 Personlized Guidance through text, video and screenshare support from our tech experts available from 10am to 8pm (Sunday Holiday). You can clarify all your doubts here"));
        android.add(new FaqModel("Will I get a certificate after Completing?", "Yes, ofcourse! After you finish all chapters, you will get a certificate. You can also submit a video of your practical project to get a Master Certificate."));
        android.add(new FaqModel("What if I don't have an Android Phone?", "No worries! After you code, there is an option to check the App online (in your computer itself - through simulation). You can use this option if you do not have a phone."));
        android.add(new FaqModel("What Android App development will my child do?", "The best part about learning here is \"No Prior coding Knowledge is Required\"\n" +
                "\n" +
                "The entire course starts from the basics on how to code and is loaded with fun experiments that your child will enjoy. Each chapter will teach a new concept to your child and let him/her build an App thats fun and useful. Once learnt all concepts, imagination is the only limitation to build more wonderful Apps and solve Problems."));
        android.add(new FaqModel("What is the Android App course fee?", "Android App Development Course for kids is just Rs. 9999. Once you buy the course online, your child can start learning online immediately and can learn anytime with our A.I. based blended learning model. The course will teach 10+ Apps of various interesting concepts along with 100+ projects that your child can do easily. It also includes certification upon completion of the course. "));


        ArrayList<FaqModel> codey = new ArrayList<>();
        codey.add(new FaqModel("What are the fun things my child will learn?",
                "Every child loves remote control cars or to create something that they can play with. This course is more fun than that - with lots of learning. They will build their robot from scratch and learn to code it with C-Language. Coding will be taught using various activities like creating a Robot Vacuum Cleaner, Obstacle Avoider Robot, warehouse robot based on line following, fire fighting robot, pit avoiding robot, self driving car and more. Your child can continue to make 100+ crazy projects with it!"));
        codey.add(new FaqModel("Is it tough for my child?",
                "Oh no! These courses have been specially designed for the respective age groups and we have seen average academic performers excel in this practical learning. Your child is sure to do wonders with this kit - Get ready to be amazed!"));
        codey.add(new FaqModel("Should my child have prior coding knowledge?",
                "Not at all! No Coding or Electronics knowledge is needed. The course will be taught from the basics and it will be complete fun for your child."));
        codey.add(new FaqModel("How is it useful for my child?",
                "Learning coding by just looking at the monitor makes imagination very difficult for a child. This course has specially been designed to make coding real with a robot. When a child sees the code in action, his/her imagination becomes real and their coding skills improve drastically. Debugging the code becomes so easy for your child. Children have also shown clear improvement in their creativity, focus, logical thinking and problem solving skills."));
        codey.add(new FaqModel("What are the items required to learn this Online?",
                "Your child will need a laptop to code. Softwares and installation procedures will be taught during the online course."));
        codey.add(new FaqModel("What is the detailed Syllabus?",
                "1.Electrical and Electronics\n" +
                        "Your child will need a laptop to code and upload into an Android phone. Softwares and installation procedures will be given by us\n\n" +
                        "2.Programming\n" +
                        "Introduction to programming/ C-Program/ Embedded-C/ Syntax/ Headers/ Variables/ Functions/ Statements – Types and Uses/ Instructions/ Conditions/ Loops/ Arduino Software/ Installing Software/ Identifying COM Port/ Setup and loop functions/ Assigning a port as Input or Output/ Writing digital outputs to a port/ Writing analog outputs/ Reading digital inputs/ Reading analog inputs/ Performing conditional actions based on inputs/ Performing continuous actions based on inputs/Concept of delay and its effects/ PWM concept and working/ Working with speed control of motors and brightness of LEDs/ Creating functions/ Calling functions/ Serial Communication/ Establishing communication/ ASCII Codes/ Programming Robots on various algorithms.\n\n" +
                        "3.Mechanical\n" +
                        "Designing of Autonomous Robots/ Parameters to be considered/ Placement of components and analysis of design.\n\n" +
                        "4.Algorithm\n" +
                        "Tabular Algorithms/ Analyzing the possible conditions/ Simulating the output/ Concept of Obstacle Avoider Robot/ Concept of Pit Avoider Robot/ Concept of Light follower Robot/ Concept of Wall follower Robot/Concept of Line follower Robot/ Concept of Computer Controlled Robot."));
        codey.add(new FaqModel("What can I do with the kit after my child finishes learning?",
                "The kit is a tool to continue experimenting more than 100+ Projects. After your child has learnt all the concepts, imagination and creativity is the only limitation to the number of projects they can do. There will also be competitions where your child can participate with this kit and showcase the skills gained!"));
        codey.add(new FaqModel("What if there are doubts while learning? How to clarify?",
                "There is 1:1 Personlized Guidance through text, video and screenshare support from our tech experts available from 10am to 8pm (Sunday Holiday). You can clarify all your doubts here"));
        codey.add(new FaqModel("Will I get a certificate after Completing?",
                "Yes, ofcourse! After you finish all chapters, you will get a certificate. You can also submit a video of your practical project to get a Master Certificate."));

        ArrayList<FaqModel> electro = new ArrayList<>();

        electro.add(new FaqModel("What are the other items required to learn with this kit?",
                "You just need a desktop computer or laptop or tablet with Internet connection to learn with this kit. Preferably, use Google Chrome browser to learn Online."
        ));
        electro.add(new FaqModel("What can I make with this kit?",
                "This kit helps you learn the Basics of Electronics – Circuit Building, Circuit Debugging and more. You get to create your own Burglar Alarm, Automatic lighting systems, Brightness Controllers, Door open detectors during the Online Learning. You can move on to create more than 50+ innovative projects with it, after completing your learning! Imagination will be your only limitation to continue innovating and creating."
        ));
        electro.add(new FaqModel("How many hours will this keep me engaged?",
                "This kit requires 16 involvement hours (which includes video time, practical time and time for assessments). If you want to keep yourself engaged more, you can always checkout the big list of projects (50+) that can be done using this kit !"
        ));
        electro.add(new FaqModel("What is the syllabus taught with this kit?",
                "Introduction to Electronics/Various Electronic Components/ Voltage/ Current/ Resistor/ Power Supply – Types and Specifications/ Voltage Regulation/ Introduction to Light Emitting Diodes/ Lighting an LED/ Series and Parallel Connections/ Introduction to Switches-Types/ SPST, SPDT/ Push-to-on, Momentary Switch/ Interfacing the Switch types/ Two way control of devices/ Concept of Potentiometer/Brightness control of LEDs/Electromagnet –concept and working/ Motor – concept and working/ Direction Control of Motor/ Introduction to Sensors – Types and use/ Light Sensor – Concept and Working/ Interfacing Light Sensor/ Light Sensor controlled LEDs/ Infrared Sensor – Concept and Working/ Interfacing IR Sensor/IR Sensor controlled buzzer."
        ));
        electro.add(new FaqModel("Will we learn to build Robots with this kit?",
                "No, you will not learn to build Robots with this kit. Robotics is a multi-disciplinary field involving Electronics, Mechanical, Programming and Algorithm. The Robotics Course is designed in the same way - with Electronics first (using Electroblocks kit), Mechanical next (using Ranger Kit), Programming next (using Codey Kit) and finally Algorithm (using Quadrino Kit). It is highly recommended to go step by step and ensure a structured way of learning. This kit gives you a strong foundation in Electronics and thereby you can step ahead to the other fields of Robotics from the next kits. You will see that this level is equally interesting – so don't think twice!"
        ));
        electro.add(new FaqModel("What can I do with the kit after I learn",
                "The Kit has been decided to teach you concepts and use it for whatever applications you want - it is not a Toy, its an explorable Tech Kit - you can keep using it for any projects, mix and match with other kits, use it in real-time applications and more."
        ));
        electro.add(new FaqModel("What if there are doubts while learning? How to clarify?",
                "There is Live Tech Support through your online account – with text, video and screenshare support. The live tech support will be available from 10am to 8pm (Monday holiday). For general info, you can always contact us – our friendly support team is always here to answer."
        ));
        electro.add(new FaqModel("Will I get a certificate after completing? Will there be tests and scores?",
                "After every chapter, there will be quick tests and scores to evaluate the conceptual understanding of that particular chapter. After all these chapter tests are taken, a Completion Certificate will be provided.\n" +
                        "\n" +
                        "If you want to get Certified with a Master Certificate, then you will have to do a project of your own (any creative idea would do), take a video of project and submit it in the e-learning platform. The project will be evaluated and Medal along with Master Certificate will be provided."
        ));
        electro.add(new FaqModel("What is the recommended Internet speed to learn online?",
                "Minimum requirement is 2Mbps download speed. Recommended Internet speed is 4Mbps download speed."
        ));

        ArrayList<FaqModel> drone = new ArrayList<>();
        drone.add(new FaqModel("What are the other items required to learn with this kit?",
                "You just need a desktop computer or laptop or tablet with Internet connection to learn with this kit. Preferably, use Google Chrome browser to learn Online."
        ));
        drone.add(new FaqModel("What can I make after I finish this course?",
                "This course will help you learn the physics behind the flying of Drones, components used in it, basics of programming and logic building. You will be equipped with the knowledge to build your own Drone and program it do help in various activities such as pick and place delivery of objects, water spraying for farming, surveillance etc. Imagination will be your only limitation to continue innovating and creating."
        ));
        drone.add(new FaqModel("How many hours will this keep me engaged?",
                "This course requires 16 involvement hours (which incorporates video time, experiment time and question-answer time). If you feel that you need to keep yourself drew in, you can simply checkout the huge rundown of projects (50+) that should be possible utilizing the skills gained from this course."
        ));
        drone.add(new FaqModel("What is the syllabus taught with this kit?",
                "Definition of UAV / Evolution of UAV/Drones / Applications of Drones / Classification of Drones based on Structure - Rotary Wing Drone, Fixed Wing Drone / Comparison of Rotary Wing and Fixed Wing Drone / Introduction to DJI Tello Drone\n" +
                        "\n" +
                        "Basic Physics Concepts - Force, Motion, Friction, Gravity / Four Forces of Flight - Thrust, Lift, Drag, Weight / Movements of flight - Roll, Pitch, Yaw\n" +
                        "\n" +
                        "Parts of Drone - Frame, Battery, Motors, Propellers, Microcontroller, ESC, Sensor Unit, Propeller Guard, Camera\n" +
                        "\n" +
                        "Uses of Protective Cage/ Cage Assembly / How to fly a Drone\n" +
                        "\n" +
                        "Why Programming is needed / What is Program and Programming Language / Comparison of Visual and Text-based programming language\n" +
                        "\n" +
                        "Introduction to Scratch / Installing Softwares on PC - Adobe AIR, Scratch, Nodejs\n" +
                        "\n" +
                        "Use of Loops / Nested Loops / Variables / If-else/ Play Sound Blocks\n" +
                        "\n" +
                        "Tello Scratch Blocks/ Fly Forward, Left, Right, Backward Blocks/ Rotate CW CCW Blocks\n" +
                        "\n" +
                        "Introduction to Coordinate System (2D, 3D)/ Plotting coordinates/ Fly in curve using coordinates"
        ));
        drone.add(new FaqModel("What can I do with the kit after I learn?",
                " You are all set to do various activities with the kit and showcase the projects among the community. However, if you do not want to learn with a kit for yourself, then you can always purchase the online course alone, or you can learn at our Maker Lab with the lab kit for a lesser cost :)"
        ));
        drone.add(new FaqModel("What if I have doubts while learning? How can I clarify my doubts?",
                "There is Live Tech Support through your online account – with text, video and screen-share support. The live tech support will be available from 10am to 8pm (Monday holiday). For general info, you can always contact us – our friendly support team is always here to answer."
        ));
        drone.add(new FaqModel("Will I get a certificate after completing? Will there be tests and scores?",
                "After each chapter, there will be simple tests and scores to assess the applied comprehension of that specific chapter. At the completion of all the Chapter tests, you will be awarded with Completion Certificate.\n" +
                        "\n" +
                        "If you need the Master Certificate, you should build a project (any inventive thought would do), take a video of it and submit it online. The video will be assessed and Medal alongside Master Certificate will be given."
        ));
        drone.add(new FaqModel("What is the recommended Internet speed to learn online?",
                "Recommended Internet speed is 4Mbps download speed"
        ));
        drone.add(new FaqModel("If I order now, when will I get the kit? And when can I start learning?",
                "Your kit will be dispatched after 20 working days from our warehouse (unless specified differently in the checkout page). We ship the kit with Blue Dart or other reputed logistics provider suitable for your location. Once you receive your kit, the procedure to activate the online course will be provided in the kit. Follow the procedure and your journey with us starts :)"
        ));
        drone.add(new FaqModel("Is Warranty available for the Tello Drone?",
                "Warranty is not applicable for the Tello Drone as it is an imported product"
        ));

        switch (courseDetails.getSlug()) {
            case "scratch-programming-online-course":
                faqAdapter = new FaqAdapter(scratch, this);
                break;

            case "android-kit":
                faqAdapter = new FaqAdapter(android, this);
                break;

            case "codey-inventor-kit":
                faqAdapter = new FaqAdapter(codey, this);
                break;

            case "electro-blocks":
                faqAdapter = new FaqAdapter(electro, this);
                break;

            case "drone-kit":
                faqAdapter = new FaqAdapter(drone, this);
                break;

        }

        recyclerView.setAdapter(faqAdapter);



    }


    private void setStaticCourseDetailsAndWhatWillYouLearn() {
        String scratch = getCourseDetails("Learn to Code an online Robot to Dance, solve Mazes and 25+ more fun projects. No Prior Coding Knowledge is required", "Lifetime Access with 1:1 Guidance", "Certification upon completion", "Upto 50+ Projects, no limitation..");
        int scratchImage = R.drawable.scratch;
        List<String> scratchListTitle = Arrays.asList("Robotic Calculator", "Voice Recognition App", "Vacuum Cleaner Robot", "Maze Solver");
        List<String> scratchList = Arrays.asList("Navigate the Robot on a grid based on the Math operations using Coding!", "Code your Dancing Robot to create some cool Dance Moves!", "Program your Robot to move like a Vacuum cleaner in a room!", "Program the Robot to move through the maze from Start to Finish");

        String android = getCourseDetails("Learn to Code & Build Android Apps like GPS based Apps, Gaming Apps, Sensor & Camera based Apps & 50+ Apps. No Prior coding knowledge needed. ", "Lifetime Access with 1:1 Guidance", "Certification upon completion", "Upto 100+ Projects, no limitation..");
        int androidImage = R.drawable.android;
        List<String> androidListTitle = Arrays.asList("Build Your Own Game", "Voice Recognition App", "GPS Locator App", "Pedometer App");
        List<String> androidList = Arrays.asList("Let your child learn to build cool Android game apps like Football!", "Let your child interact with the computer using voice recognition", "Let them acquire the skills to build a Live Location tracker", "A simple application to track the step count while we walk");

        String codey = getCourseDetails("Learn to Code (C-Language) with Codey Robot. Fire-fighting Robot, Robot Vacuum Cleaner & 100+ fun Projects. No Prior coding knowledge needed. ", " Programmable Robotic Maker Kit ", "Lifetime Access with 1:1 Guidance", "Certification upon completion", "Upto 50+ Projects, no limitation..");
        int codeyImage = R.drawable.codey;
        List<String> codeyListTitle = Arrays.asList("Cleaning Robot", "Drawing Robot", "Gardening Robot", "Automatic Parking");
        List<String> codeyList = Arrays.asList("Let your child attach a mop to the Robot and code it to clean your house in a perfect algorithm", "Let your child give a pen to the Robot and code it to draw your name or a pattern of choice", "Let your child code the Robot to follow the road/line perfectly and water the garden alongside", "Let the Robot be coded to sense the walls of a garage and park itself neatly");

        String electro = getCourseDetails("Learn & Experiment with sensors, motors, & more. Build Burglar Alarms, Automatic Lighting systems, Security systems & 50+ fun circuits. ", "Modular Electronic Maker Kit", "Lifetime Access with 1:1 Guidance", "Certification upon completion", "Upto 50+ Projects, no limitation..");
        int electroImage = R.drawable.electro;
        List<String> electroListTitle = Arrays.asList("Electro-Block", "Electro-Block", "Electro-Block", "Electro-Block");
        List<String> electroList = Arrays.asList("The fundamentals to ensure a solid footing in the tech-world!", "Practical understanding of basic electronics, circuit building and circuit debugging techniques.", "Conceptual understanding of theories related to Voltage, LEDs, Switch mechanisms, Sensor controlled buzzers and more…", "With over 75 real-world related experiments to develop - a truly experiential induction into electronics!");

        String drone = getCourseDetails("Be a part of the revolution of drones, which are becoming commonplace in delivery and agricultural sectors. Visualize and learn drone physics with Scratch, Block-based Visual Programming language. ", "Programmable Drone Kit", "Lifetime Access with 1:1 Guidance", "Certification upon completion", "Upto 50+ Projects, no limitation..");
        int droneImage = R.drawable.drone;
        List<String> droneListTitle = Arrays.asList("LOGICAL THINKING", "DEBUGGING", "PHYSICS", "CODING");
        List<String> droneList = Arrays.asList("Enter the world of Drones with the extensively programmable Tello Drone.", "Conceptual understanding of Drone physics such as forces of flight, flight movement terms such as roll, pitch and yaw, components of a Drone etc. ", "Learn the basics of programming in a playful, interactive way using Scratch - the most popular block-based visual programming language ", "Learn about building logics to program your Drone to serve various applications such as Farming, Security, Transportation, etc. ");


        List<CourseDetailsStaticModel> courseDetailsStaticModels = new ArrayList<>();
        courseDetailsStaticModels.add(new CourseDetailsStaticModel(scratch, scratchImage, scratchList, scratchListTitle));
        courseDetailsStaticModels.add(new CourseDetailsStaticModel(android, androidImage, androidList, androidListTitle));
        courseDetailsStaticModels.add(new CourseDetailsStaticModel(codey, codeyImage, codeyList, codeyListTitle));
        courseDetailsStaticModels.add(new CourseDetailsStaticModel(electro, electroImage, electroList, electroListTitle));
        courseDetailsStaticModels.add(new CourseDetailsStaticModel(drone, droneImage, droneList, droneListTitle));

        switch (courseDetails.getSlug()) {
            case "scratch-programming-online-course":
                setTextImageAndWhatWillYouLearn(courseDetailsStaticModels.get(0));
                break;

            case "android-kit":
                setTextImageAndWhatWillYouLearn(courseDetailsStaticModels.get(1));
                break;

            case "codey-inventor-kit":
                setTextImageAndWhatWillYouLearn(courseDetailsStaticModels.get(2));
                break;

            case "electro-blocks":
                setTextImageAndWhatWillYouLearn(courseDetailsStaticModels.get(3));
                break;

            case "drone-kit":
                setTextImageAndWhatWillYouLearn(courseDetailsStaticModels.get(4));
                break;

        }
    }

    private void setTextImageAndWhatWillYouLearn(CourseDetailsStaticModel courseDetailsStaticModels) {
        imageViewCourseDetails.setImageResource(courseDetailsStaticModels.getCourseImageId());
        textViewCourseDetails.setText(Html.fromHtml(courseDetailsStaticModels.getCourseDetails()));

        firstItemButton.setText(courseDetailsStaticModels.getWhatWillMyChildLearnTitle().get(0));
        secondItemButton.setText(courseDetailsStaticModels.getWhatWillMyChildLearnTitle().get(1));
        thirdItemButton.setText(courseDetailsStaticModels.getWhatWillMyChildLearnTitle().get(2));
        fourthItemButton.setText(courseDetailsStaticModels.getWhatWillMyChildLearnTitle().get(3));

        firstItemText.setText(courseDetailsStaticModels.getWhatWillMyChildLearn().get(0));
        secondItemText.setText(courseDetailsStaticModels.getWhatWillMyChildLearn().get(1));
        thirdItemText.setText(courseDetailsStaticModels.getWhatWillMyChildLearn().get(2));
        fourthItemText.setText(courseDetailsStaticModels.getWhatWillMyChildLearn().get(3));
    }

    private String getCourseDetails(String title, String... items) {
        String demo = "<p>Learn to Code (C-Language) with Codey Robot. Fire-fighting Robot, Robot Vacuum Cleaner &amp; 100+ fun Projects. No Prior coding knowledge needed.</p>\n" +
                "<div class=\"feature-title\">Includes:</div>\n" +
                "<ul>\n" +
                "    <li>\n" +
                "        <div class=\"feature\">Programmable Robotic Maker Kit</div>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "        <div class=\"feature\">Lifetime Access with 1:1 Guidance</div>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "        <div class=\"feature\">Upto 100+ Projects, no limitation..</div>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "        <div class=\"feature\" id=\"isPasted\">Certification upon completion</div>\n" +
                "    </li>\n" +
                "</ul>";
        String paraTitle = "<p>" + title + "</p>\n";
        StringBuilder stringBuilder = new StringBuilder();
        for (String itemList : items) {
            stringBuilder.append("    <li>\n");
            stringBuilder.append("        <div class=\"feature\">&nbsp;").append(itemList).append("</div>\n");
            stringBuilder.append("    <li>\n");
        }
        String finalItem = stringBuilder.toString();
        return paraTitle +
                "<div class=\"feature-title\">Includes:</div>\n" +
                "<ul>\n" +
                finalItem +
                "</ul>";
    }

    private void findViewById() {
        ll_preview = findViewById(R.id.ll_preview);

        tabLayout = findViewById(R.id.simpleTabLayout);
        frameLayout = findViewById(R.id.simpleFrameLayout);
        imageButtonBack = findViewById(R.id.back_button);
        materialCardViewEnquire = findViewById(R.id.course_details_enquire_now);
        materialCardViewBuy = findViewById(R.id.course_details_buy_now);
        textViewCourseDetails = findViewById(R.id.course_details_text);
        imageViewCourseDetails = findViewById(R.id.course_details_image);


        course_details_title = findViewById(R.id.course_details_title);
        course_details_age = findViewById(R.id.course_details_age);
        course_details_money = findViewById(R.id.course_details_money);

        firstItemButton = findViewById(R.id.firstItemButton);
        secondItemButton = findViewById(R.id.secondItemButton);
        thirdItemButton = findViewById(R.id.thirdItemButton);
        fourthItemButton = findViewById(R.id.fourthItemButton);

        firstItemLayout = findViewById(R.id.firstItemLayout);
        secondItemLayout = findViewById(R.id.secondItemLayout);
        thirdItemLayout = findViewById(R.id.thirdItemLayout);
        fourthItemLayout = findViewById(R.id.fourthItemLayout);

        firstItemText = findViewById(R.id.firstItemText);
        secondItemText = findViewById(R.id.secondItemText);
        thirdItemText = findViewById(R.id.thirdItemText);
        fourthItemText = findViewById(R.id.fourthItemText);

        recyclerView = findViewById(R.id.faqRecycler);
        faqTextDetails = findViewById(R.id.faqTextDetails);

        call_phone = findViewById(R.id.call_phone);
        whatsapp = findViewById(R.id.whatsapp);

        setData();
        onClick();
    }

    private void onClick() {

        firstItemButton.setOnClickListener(v -> {
            if (firstItemLayout.isExpanded()) {
                firstItemLayout.collapse();
                firstItemButton.setIcon(getDrawable(R.drawable.ic_round_arrow_forward_ios_24));
            } else {
                firstItemButton.setIcon(getDrawable(R.drawable.ic_down));
                firstItemLayout.expand();
            }
        });

        secondItemButton.setOnClickListener(v -> {
            if (secondItemLayout.isExpanded()) {
                secondItemLayout.collapse();
                secondItemButton.setIcon(getDrawable(R.drawable.ic_round_arrow_forward_ios_24));
            } else {
                secondItemButton.setIcon(getDrawable(R.drawable.ic_down));
                secondItemLayout.expand();
            }
        });

        thirdItemButton.setOnClickListener(v -> {
            if (thirdItemLayout.isExpanded()) {
                thirdItemLayout.collapse();
                thirdItemButton.setIcon(getDrawable(R.drawable.ic_round_arrow_forward_ios_24));
            } else {
                thirdItemButton.setIcon(getDrawable(R.drawable.ic_down));
                thirdItemLayout.expand();
            }
        });

        fourthItemButton.setOnClickListener(v -> {
            if (fourthItemLayout.isExpanded()) {
                fourthItemLayout.collapse();
                fourthItemButton.setIcon(getDrawable(R.drawable.ic_round_arrow_forward_ios_24));
            } else {
                fourthItemButton.setIcon(getDrawable(R.drawable.ic_down));

                fourthItemLayout.expand();
            }
        });


        materialCardViewBuy.setOnClickListener(v -> {

            if (!SessionManager.isLoggedIn()) {
                bottomSheetDialogForPhone.show();
            } else {
                // go to cart page
                getCartData();
                Logger.d("I am here");
            }

        });


    }

    private void setData() {

        course_details_title.setText(courseDetails.getName());
        faqTextDetails.setText(courseDetails.getName() + " FAQ");
        // course_details_age.setText(courseDetails.getAgeCategory().get(0));
        course_details_money.setText("INR " + courseDetails.getPrice().get(0));

        Glide.with(getApplicationContext()).load(courseDetails.getMobile_app_image()).placeholder(getResources().getDrawable(R.drawable.sprobotics_recyclerview)).into(imageViewCourseDetails);

        boolean comingFromCourseAdapter = getIntent().getBooleanExtra("comingFromCourseAdapter", false);

        if (comingFromCourseAdapter) {
            switch (courseDetails.getAgeCategory().get(0)) {
                case "Junior":
                    course_details_age.setText("7+");
                    break;
                case "Senior":
                    course_details_age.setText("10+");
                    break;

                default:
                    course_details_age.setText("13+");
            }
        } else {
            course_details_age.setText(courseDetails.getAge_group());
        }


    }

    private void setTabLayout(Bundle savedInstanceState) {
        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText("Course Details");
        tabLayout.addTab(firstTab);

        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText("What will you learn");
        tabLayout.addTab(secondTab);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new CourseDetailsFragment();
                        break;
                    case 1:
                        fragment = new CourseLearnFragment();
                        break;
                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.simpleFrameLayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tabLayout.getTabAt(0).select();
        tabLayout.getTabAt(1).select();
    }

    /*private void setBottomSheet() {

        bottomSheetDialogForOtp = new BottomSheetDialog(this, R.style.BottomSheetDialogThemeNoFloating);
        bottomSheetDialogForEmail = new BottomSheetDialog(this, R.style.BottomSheetDialogThemeNoFloating);


        bottomSheetDialogForOtp.setContentView(R.layout.bottomsheet_otp_picker);
        bottomSheetDialogForEmail.setContentView(R.layout.bottomsheet_email_picker);


        MaterialButton gotoEmail = bottomSheetDialogForOtp.findViewById(R.id.gotoEmail);
        MaterialButton gotoPhone = bottomSheetDialogForEmail.findViewById(R.id.gotoPhone);


        if (gotoEmail != null)
            gotoEmail.setOnClickListener(v -> {
                bottomSheetDialogForOtp.dismiss();
                bottomSheetDialogForEmail.show();
            });

        if (gotoPhone != null)
            gotoPhone.setOnClickListener(v -> {
                bottomSheetDialogForEmail.dismiss();
                bottomSheetDialogForPhone.show();
            });


        *//*OTP*//*
        OtpView otpView = bottomSheetDialogForOtp.findViewById(R.id.otp_view);
        TextView textView = bottomSheetDialogForOtp.findViewById(R.id.otp_text);
        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                if (OTP.equalsIgnoreCase(otp)) {
                    bottomSheetDialogForOtp.dismiss();
                    MethodClass.hideKeyboard(activity);
                    MethodClass.showAlertDialog(activity, true, "OTP verified", "OTP verified successfully", false);
                    loginWithEmailOrMobile();
                } else
                    MethodClass.showAlertDialog(activity, true, "Invalid OTP", "Invalid OTP", false);


            }
        });
        MaterialButton materialButtonContinueOtp = bottomSheetDialogForOtp.findViewById(R.id.otp_continue);
        materialButtonContinueOtp.setOnClickListener(v -> {

        });
        *//*Email*//*
        TextInputEditText textInputEditTextEmail = bottomSheetDialogForEmail.findViewById(R.id.bottom_email);
        TextInputEditText textInputEditTextPassword = bottomSheetDialogForEmail.findViewById(R.id.bottom_password);
        MaterialButton materialButtonContinueEmail = bottomSheetDialogForEmail.findViewById(R.id.button_continue);
    }*/


    private void initLoginBottomSheet() {
        bottomSheetDialogForPhone = new BottomSheetDialog(this, R.style.BottomSheetDialogThemeNoFloating);
        bottomSheetDialogForPhone.setContentView(R.layout.bottomsheet_countrycode_picker);

        MaterialButton gotoOtp = bottomSheetDialogForPhone.findViewById(R.id.gotoOtp);
        EditText editText_carrierNumber = bottomSheetDialogForPhone.findViewById(R.id.editText_carrierNumber);

        editText_carrierNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 10) {

                    if (s.toString().startsWith("6") || s.toString().startsWith("7") || s.toString().startsWith("8") || s.toString().startsWith("9")) {
                        loginType = "M";
                        sentOTPRequest(s.toString());
                    } else {
                        ToastUtils.showLong(CourseDetailsActivity.this, "Enter a valid Mobile Number");
                    }
                }
            }
        });


    }

    public void loginWithEmailOrMobile() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", SessionManager.getValue(SessionManager.CHILD_NAME));
        map.put("child_age", SessionManager.getValue(SessionManager.CHILD_AGE));
        map.put("mobile", mobile);
        apiRequest.postRequest(MOBILE_LOGIN, map, MOBILE_LOGIN);


    }

    public void requestForMobileOTP(String mobile) {
        this.mobile = mobile;
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        apiRequest.postRequest(MOBILE_OTP, map, MOBILE_OTP);
    }

    public void getCartData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("customer_id", SessionManager.getLoginResponse().getData().getCustomerId());
        apiRequest.postRequest(GET_CART, map, GET_CART);

    }

    @Override
    public void OnCallBackSuccess(String tag, String response) {
        super.OnCallBackSuccess(tag, response);

        if (tag.equalsIgnoreCase(GET_CART)) {
            CartResponse response1 = (CartResponse) GsonUtil.toObject(response, CartResponse.class);

            if (response1.getData().size() == 0) {

                HashMap<String, String> map = new HashMap<>();
                map.put("customer_id", SessionManager.getLoginResponse().getData().getCustomerId());
                map.put("total_local_price", courseDetails.getPrice().get(0));
                map.put("billing_address_id", "");
                map.put("delivery_address_id", "");
                map.put("cart_item_id", "");
                map.put("cart_id", "");
                map.put("product_id", "" + courseDetails.getProductId());
                map.put("quantity", "1");
                map.put("local_price", courseDetails.getPrice().get(0));
                apiRequest.postRequest(ADD_CART, map, ADD_CART);


            } else {

                String courseId = "";

                for (com.sproboticworks.model.cartrespone.DataItem dataItem : response1.getData()) {

                    if (dataItem.getProductId().equalsIgnoreCase("" + courseDetails.getProductId())) {
                        courseId = dataItem.getItemId();
                        break;
                    }

                }


                int totalPrice = (int) (Double.parseDouble(response1.getData1().getProductTotalPrice()) + Double.parseDouble(courseDetails.getPrice().get(0)));

                if (courseId.equals("")) {

                    HashMap<String, String> map = new HashMap<>();
                    map.put("customer_id", SessionManager.getLoginResponse().getData().getCustomerId());
                    map.put("total_local_price", "" + totalPrice);
                    map.put("billing_address_id", "");
                    map.put("delivery_address_id", "");
                    map.put("cart_item_id", "");
                    map.put("cart_id", "");
                    map.put("product_id", "" + courseDetails.getProductId());
                    map.put("quantity", "1");
                    map.put("local_price", courseDetails.getPrice().get(0));
                    apiRequest.postRequest(ADD_CART, map, ADD_CART);

                } else {

                    Toast.makeText(CourseDetailsActivity.this, "You already have this course in your cart", Toast.LENGTH_SHORT).show();
                    MethodClass.go_to_next_activity(CourseDetailsActivity.this, CartActivity.class);

                }


            }


        }
        if (tag.equalsIgnoreCase(ADD_CART)) {

            try {
                JSONObject object = new JSONObject(response);
                Toast.makeText(CourseDetailsActivity.this, object.getString("data"), Toast.LENGTH_SHORT).show();
                MethodClass.go_to_next_activity(CourseDetailsActivity.this, CartActivity.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        /*if (tag.equalsIgnoreCase(MOBILE_OTP)) {
            PhoneOtpSentResponse response1 = (PhoneOtpSentResponse) GsonUtil.toObject(response, PhoneOtpSentResponse.class);
            ToastUtils.showLong(activity, response1.getData().getOtp());
            OTP = response1.getData().getOtp();
            bottomSheetDialogForPhone.dismiss();
            bottomSheetDialogForOtp.show();
        }*/
        /*if (tag.equalsIgnoreCase(MOBILE_LOGIN)) {
            LogInResponse response1 = (LogInResponse) GsonUtil.toObject(response, LogInResponse.class);
            SessionManager.setValue(SessionManager.LOGIN_RESPONSE, GsonUtil.toJsonString(response1));
            SessionManager.setLoggedIn(true);
        }*/

        if (tag.equalsIgnoreCase(EMAIL_OTP)) {
            PhoneOtpSentResponse response1 = (PhoneOtpSentResponse) GsonUtil.toObject(response, PhoneOtpSentResponse.class);
            // ToastUtils.showLong(activity, response1.getData().getOtp());
            OTP = response1.getData().getOtp();
            //bottomSheetDialogForPhone.dismiss();
            textview_otp_sent_to.setText("OTP has been sent to " + email);
            gotoEmail.setText("Proceed with Mobile Number");
            bottomSheetDialogForOtp.show();
        }
        if (tag.equalsIgnoreCase(MOBILE_LOGIN)) {
            LogInResponse response1 = (LogInResponse) GsonUtil.toObject(response, LogInResponse.class);
            SessionManager.setValue(SessionManager.LOGIN_RESPONSE, GsonUtil.toJsonString(response1));
            SessionManager.setLoggedIn(true);
            ToastUtils.showLong(activity, "Logged in successfully");
        }
    }

    public void requestForEmailOtp(String email_id) {
        this.email = email_id;
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email_id);
        apiRequest.postRequest(EMAIL_OTP, map, EMAIL_OTP);
    }


    private void setBottomSheet() {

        bottomSheetDialogForOtp = new BottomSheetDialog(activity, R.style.BottomSheetDialogThemeNoFloating);
        bottomSheetDialogForEmail = new BottomSheetDialog(activity, R.style.BottomSheetDialogThemeNoFloating);


        bottomSheetDialogForOtp.setContentView(R.layout.bottomsheet_otp_picker);
        bottomSheetDialogForEmail.setContentView(R.layout.bottomsheet_email_picker);


        gotoEmail = bottomSheetDialogForOtp.findViewById(R.id.gotoEmail);
        MaterialButton gotoPhone = bottomSheetDialogForEmail.findViewById(R.id.gotoPhone);

        MaterialButton gotoEmailAgain = bottomSheetDialogForPhone.findViewById(R.id.login_using_phone);

        gotoEmailAgain.setOnClickListener(v -> {
            bottomSheetDialogForEmail.show();
            bottomSheetDialogForPhone.dismiss();
        });

        if (gotoEmail != null)
            gotoEmail.setOnClickListener(v -> {

                if (loginType.equalsIgnoreCase("M"))
                    bottomSheetDialogForEmail.show();
                else bottomSheetDialogForPhone.show();
                bottomSheetDialogForOtp.dismiss();

            });

        if (gotoPhone != null)
            gotoPhone.setOnClickListener(v -> {
                bottomSheetDialogForEmail.dismiss();
                bottomSheetDialogForPhone.show();
            });


        /*OTP*/
        OtpView otpView = bottomSheetDialogForOtp.findViewById(R.id.otp_view);
        TextView textView = bottomSheetDialogForOtp.findViewById(R.id.otp_text);
        TextView textview_resend_otp = bottomSheetDialogForOtp.findViewById(R.id.textview_resend_otp);
        textview_otp_sent_to = bottomSheetDialogForOtp.findViewById(R.id.textview_otp_sent_to);
        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                if (loginType == "M")
                    OTP_Verification(otp);
                else {
                    if (OTP.equalsIgnoreCase(otp)) {
                        if (bottomSheetDialogForEmail.isShowing())
                            bottomSheetDialogForEmail.dismiss();
                        bottomSheetDialogForOtp.dismiss();
                        MethodClass.hideKeyboard(activity);
                        MethodClass.showAlertDialog(activity, true, "OTP verified", "OTP verified successfully", false);
                        loginWithEmailOrMobile("E");

                    } else
                        MethodClass.showAlertDialog(activity, true, "Invalid OTP", "Invalid OTP", false);
                }


            }
        });
        MaterialButton materialButtonContinueOtp = bottomSheetDialogForOtp.findViewById(R.id.otp_continue);
        materialButtonContinueOtp.setOnClickListener(v -> {

        });
        textview_resend_otp.setOnClickListener(v -> {
            if (loginType == "M")
                sentOTPRequest(phone_number);
        });
        /*Email*/
        TextInputEditText textInputEditTextEmail = bottomSheetDialogForEmail.findViewById(R.id.bottom_email);
        TextInputEditText textInputEditTextPassword = bottomSheetDialogForEmail.findViewById(R.id.bottom_password);
        MaterialButton materialButtonContinueEmail = bottomSheetDialogForEmail.findViewById(R.id.button_continue);

        materialButtonContinueEmail.setOnClickListener(v -> {
            if (textInputEditTextEmail.getText().toString().length() > 0) {
                loginType = "E";
                requestForEmailOtp(textInputEditTextEmail.getText().toString());
            }
        });
    }

    public void sentOTPRequest(String phoneNumber) {
        progressDialog.show();
        phone_number = phoneNumber;


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNumber,                     // Phone number to verify
                60,                           // Timeout duration
                TimeUnit.SECONDS,                // Unit of timeout
                activity,        // Activity (for callback binding)
                mCallback);
    }


    public void OTP_Verification(String otptext) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otptext);
        signInWithPhoneAuthCredential(credential, otptext);
    }

    public void loginWithEmailOrMobile(String type) {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", SessionManager.getValue(SessionManager.CHILD_NAME));
        map.put("child_age", SessionManager.getValue(SessionManager.CHILD_AGE));
        if (type.equalsIgnoreCase("M"))
            map.put("mobile", phone_number);
        else map.put("email", email);
        apiRequest.postRequest(type.equalsIgnoreCase("M") ? MOBILE_LOGIN : EMAIL_LOGIN, map, MOBILE_LOGIN);
    }

    private void startFirebaseLogin() {
        FirebaseApp.initializeApp(activity);
        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NotNull PhoneAuthCredential phoneAuthCredential) {
                //System.out.println ("====verification complete call  " + phoneAuthCredential.getSmsCode ());
                progressDialog.dismiss();
                if (bottomSheetDialogForPhone.isShowing())
                    bottomSheetDialogForPhone.dismiss();
                if (bottomSheetDialogForOtp.isShowing())
                    bottomSheetDialogForOtp.dismiss();

                loginWithEmailOrMobile("M");
            }

            @Override
            public void onVerificationFailed(@NotNull FirebaseException e) {
                setSnackBar(e.getLocalizedMessage(), getString(R.string.btn_ok), "failed");
                Toast.makeText(activity.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(activity.getApplicationContext(), getString(R.string.otp_sent), Toast.LENGTH_SHORT).show();

                if (bottomSheetDialogForPhone.isShowing())
                    bottomSheetDialogForPhone.dismiss();

                if (!bottomSheetDialogForOtp.isShowing())
                    bottomSheetDialogForOtp.show();
                gotoEmail.setText("Proceed with Email ID");


                textview_otp_sent_to.setText("OTP has been sent to +91" + phone_number);
                progressDialog.dismiss();

            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, final String otptext) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            loginWithEmailOrMobile("M");
                            if (bottomSheetDialogForOtp.isShowing())
                                bottomSheetDialogForOtp.dismiss();
                            if (bottomSheetDialogForPhone.isShowing())
                                bottomSheetDialogForPhone.dismiss();

                        } else {

                            //verification unsuccessful.. display an error message
                            String message = "Something is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                            ToastUtils.showLong(activity, message);

                        }
                    }
                });
    }

    public void setSnackBar(String message, String action, final String type) {
        final Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equals("reset_pass") || type.equals("forgot") || type.equals("register")) {
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                snackbar.dismiss();
            }
        });
        snackbar.setActionTextColor(Color.RED);
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setMaxLines(5);
        snackbar.show();
    }
}
/*
* startActivity(new Intent(this, EnquiryActivity.class).
                    putExtra("bottomTag", "page_1").
                    putExtra("comingFromCourseDetails", true).
                    putExtra("course", courseDetails.getProductId()).
                    putExtra("courseName", courseDetails.getName())
            );
            overridePendingTransition(0, 0);
            finish();*/