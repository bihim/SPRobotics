package com.sproboticworks.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.sproboticworks.R;
import com.sproboticworks.adapter.TeamMembersAdapter;
import com.sproboticworks.model.TeamMembersStaticModel;

import java.util.ArrayList;

public class AboutUsActivity extends AppCompatActivity {

    private TeamMembersAdapter teamMembersAdapter;
    private TeamMembersAdapter teamMembersAdapterBoard;
    private ArrayList<TeamMembersStaticModel> teamMembersStaticModelArrayList = new ArrayList<>();
    private ArrayList<TeamMembersStaticModel> teamMembersStaticModelArrayListBoard = new ArrayList<>();
    private RecyclerView recyclerViewTeam, recyclerViewBoard;
    private TextView whatsDifferentAboutUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        recyclerViewTeam = findViewById(R.id.key_team_members);
        recyclerViewBoard = findViewById(R.id.board_members);
        whatsDifferentAboutUs = findViewById(R.id.whatsDifferentAboutUs);
        whatsDifferentAboutUs.setText(Html.fromHtml("<ul><li>We actually <strong>&apos;care&apos;</strong> about your child&apos;s future and overall holistic development! Our active Community of 10,000+ Makers ensure that!</li><li>We don&apos;t want your child to stop having <strong>FUN while Learning</strong> - which is why we have <strong>LIFETIME COURSE ACCESS</strong> for our students &amp; Courses!</li><li>And any time your child wants, we have an Expert ready on call to discuss and clarify doubts!! All day through the week(except Mondays!) from 10AM - 8PM, your child can access LIVE support like no other!</li></ul><p>Besides this, loads of benefits like Badges and Coins for your child, Merit based certificates and a thriving Community to take part in Competitions and Project Challenges to actively mentor and take care of your child&apos;s Learning future!Why wait, when the change can happen now?! Join our SP Robotic Works Community and get your child started with the best!</p>\n"));

        teamMembersStaticModelArrayList.add(new TeamMembersStaticModel("Sneha Priya", "Co-Founder\nCEO", "She started the business career at 19, back-packing across the country, going hands-on, participating and winning in national and international competitions in Robotics, Engineering and STEM. This led to the creation of AI led Tech Platform for Online and Assisted Online Education in Robotics, Engineering and STEM. Taking the first letter of her name, and that of her husband's (Pranavan) and combining it with mutual passion Robotics, 'SP Robotic Works' was created. She was one of the youngest women to be awarded as 'Iconic Women' by the Women Economic Forum. From spearheading the development of curriculum and teamware to chartering the growth of the community, she is a driving force behind all of the company’s initiatives.", R.drawable.ceo));
        teamMembersStaticModelArrayList.add(new TeamMembersStaticModel("Pranavan", "Co-Founder\nCTO", "He is equally passionate about Robotics, Engineering and STEM as his better half Sneha Priya. He holds a record of 40+ national and International awards to his name to prove it. He is the driving force behind the conceptualization and realization of every idea from the stables of SP Robotic Works. \"Thinking big\" might actually be something this person is born with. In his leadership and directions, the products and processes have been made highly scalable, empowering the entire team to run faster towards the vision of SP Robotic Works which is - To make Robotics, Engineering and STEM Education ubiquitous across India and Globe.", R.drawable.cto));
        teamMembersStaticModelArrayList.add(new TeamMembersStaticModel("Aarthi Muralitharan", "Chief Community Officer\nCCO", "Being a passionate Roboteer herself, she firmly believes in uplifting the student Community of SP Robotic Works. She leads the Student Community with the single minded objective of helping students connect with other like minded people, showcasing their projects, and making learning outcomes in Robotics, Engineering and STEM the tangible for the users. Post her corporate stint at Schneider Electric for 3 years, she rejoined SP Robotic Works to extend her desire to spread Knowledge above Robotics, Engineering and STEM through Online and Offline Community outreach programs. ", R.drawable.cco));
        teamMembersAdapter = new TeamMembersAdapter(teamMembersStaticModelArrayList, this);
        recyclerViewTeam.setAdapter(teamMembersAdapter);

        teamMembersStaticModelArrayListBoard.add(new TeamMembersStaticModel("Nagaprakasam", "Board of Director", "A versatile leader with more than 2 decades of experience in collaborating with young entrepreneurs in the US and in India. He believes in a hands-on approach and is widely sought after as a thought leader at Impact space. His personal mantra is to assist Start-ups that focus on India’s strength – people, problem, Tech. Being a lead investor, and with his expertise, he guides team SPRW through the challenges of a startup and is a strong pillar of support at every step of scaling.", R.drawable.board));
        teamMembersStaticModelArrayListBoard.add(new TeamMembersStaticModel("Dr. Jayaram K Iyer", "Board of Director", "Dr. Jayaram K Iyer holds a PhD in Marketing and Data Sciences with course work from MIT Sloan, Harvard Business School and Boston University. He is an accomplished Professional in the field of Artificial Intelligence and Analytics. He has founded several firms in the area of AI and Computer Vision. He has been a CEO of parentcircle.com and Chief Strategy and Analytics Officer of matrimony.com. He has pioneered several Artificial and Machine Learning applications and works with Team SPRW to strengthen its AI led Learning Platform SPARKY. ", R.drawable.board2));
        teamMembersStaticModelArrayListBoard.add(new TeamMembersStaticModel("MV Subramanian", "Board of Advisor", "A professional entrepreneur whose vision and business acumen have been crucial in mentoring, coaching and guiding several business ventures. His experience in management spans over 25 years with an in-depth knowledge of all areas of organizational development and management. Being a lead investor at SP Robotic Work from Indian Angel Network and being an entrepreneur himself, he guides the team SPRW with the entrepreneurial challenges and to follow best practices at every stride. ", R.drawable.advisor));
        teamMembersAdapterBoard = new TeamMembersAdapter(teamMembersStaticModelArrayListBoard, this);
        recyclerViewBoard.setAdapter(teamMembersAdapterBoard);

    }
}