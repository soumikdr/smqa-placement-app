## Meeting Outcome - 20th October, 2024

### Summary

During our initial group meeting, we finalized our research paper topic after reviewing our previous selections. We conducted preliminary research into the project topic and discussed potential features for implementation.

---

## Meeting Outcome - 27th October, 2024

### Summary

In this group meeting, we delved into project requirements, including user entities, database selection, and application type. We divided user entities (recruiter, applicant) among group members and assigned the task of developing individual user stories for the next meeting.

--- 

## Meeting Outcome - 3rd November, 2024

### Summary

During this meeting, we collectively reviewed the developed user stories, assessing their alignment with project goals and requirements. We finalized a total of 50 user stories. To estimate development effort, we utilized the planning poker game to assign story points using fibonacci nummbers with minimum value of 1 and maximum value of 13 to half of the total user stories.

---

## Meeting Outcome - 9th November, 2024

### Summary

In this group meeting, we continued the planning poker game to assign story points to the remaining user stories. We then grouped these stories into 17 distinct activities or tasks (A-Q) for inclusion in our PERT chart. 

### Images

Few screenshots are provided below from our planning poker game that we have conducted,

![User Story - 23](https://i.imgur.com/tjF0XJt.png)
![User Story - 30](https://i.imgur.com/XpPkjIu.png)
![User Story - 38](https://i.imgur.com/jIYCNGP.png)
![User Story - 41](https://i.imgur.com/XP1Cpz1.png)

---

## Meeting Outcome - 10th November, 2024

### Summery

As we finalized on our user story categories (activities), we designed network flow diagram (PERT Chart) and have done the calculations for it in this meeting. The critical path is also generated in the PERT chart. For the duration unit, we have decided to go with 'DAY' rather than weeks to make it look like more understandable. <br /><br />We have also used basic COCOMO to predict the cost of our software development project. To estimate the effort E, the development time D and the required number of people P using COCOMO, we supposed the development context is chosen as organic. <br /><br />In the meeting, it is also decided that we will start our project sprints starting from 19th November, each sprint spanning for 2 weeks.

### Images

PERT Chart calculations,

![Imgur](https://i.imgur.com/yJMDnws.png)
![Imgur](https://i.imgur.com/rX6ese1.png)
![Imgur](https://i.imgur.com/r5TYWhX.png)

Final PERT Chart,

![Imgur](https://i.imgur.com/thHzSWg.png)

COCOMO calculations,

![Imgur](https://i.imgur.com/mSRDSve.png) <br />
![Imgur](https://i.imgur.com/TLIBPjB.png)

## Meeting Outcome - 19th November, 2024

### Summary

Before we start our first sprint, we started out sprint planning by allocating user stories to the sprints based on PERT chart activities, considering the predecessor dependencies and the estimated times. Each user story is assigned to a priority level. Also, user story issues assigned among us keeping equal work distribution in mind. We decided to push every user story when unit test of user story also completed. By this way, we can reduce the costs in future sprints. We assigned a scrum master for managing the meetings. We discussed how our code architecture should be and how we must continue. We decided on how our code implementation and GitHub flow such as branch naming's and flow between feature, develop and main branches should be. 

## Sprint Meeting - 3rd December, 2024

### Summary

During our first sprint review & sprint retrospective meeting, We discussed every issue that we faced in sprint 1. We decided to keep our test cases simple but for some unit test cases, there was a recursion issue we were facing, and we decided to use Mockito library. We reviewed each other's push request and added comments if changes of code are needed. There were a few user stories that were not finished because of errors from different development environments and merge conflicts between other team member's branches. We have written Whitebox (Statement & Branch testing) unit tests for every user story. Also, to be sure that how we should adjust BlackBox testing to our code architecture, we asked our module convenor to get better clarification about the test approaches based on our project scenario. We took his suggestions for the upcoming sprint, so that it becomes more efficient, and the sprint goals become achievable.

## Sprint Meeting - 17th December, 2024

### Summary

In our second sprint review & sprint retrospective meeting, we reviewed each others progress. And discussed on how we can make use of utility functions to mitigate conflicts in our codebase. We planned how we should be doing blackbox test approaches. For, Random Based testing we have decided to use Randoop, which is a unit test generator for Java. It automatically creates unit tests for the classes in JUnit format. And for Specification Based blackbox testing, we decided to use Category Partition Method by utilizing the TSL generator tool. For test coverage, our dicision was to have at least 80% code coverage which is a real world software systems standard.

## Sprint Meeting - 31st December, 2024

### Summary
By the end of our 3rd sprint, we completed majority percentage of our project. During our sprint discussion we came to observe that we needed to come to a common ground about the test cases. So, we cleared each others confusion about the test cases in this meeting. As most of our user stories were implemented, we also came to notice that there were a few logical errors between our implementations. It was preventing us from integrating the whole project. So, we focused on resolving those and complete our test approaches.
