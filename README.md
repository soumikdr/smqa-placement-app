# Placement Application

Placement Application is a full-fledged console based application that streamlines the entire placement journey by providing a structured approach to handling applications and maintaining smooth interactions between applicants and recruiters.

---

## Prerequisites

Before running the project, ensure you have the following installed:

- Eclipse IDE
- Java Development Kit (JDK) 17

---

### Import the Project into Eclipse

1. Open Eclipse and navigate to `File > Import > General > Existing Projects into Workspace > Select Root Directory >  Browse`.
2. Select the project `root` directory of extracted zip and import it.

---
## Testing

#### *ATTENTION*: Since the necessary inputs in the project are taken via the console, if the tests need to be run in a way that includes black box tests, such as running all the tests together or only running blackbox tests, the steps specified in the `Utility.java` must be applied. Because in the tests produced with Randoop, input is expected from the console.

## White Box Testing


### Running White Box Testing

To perform white box testing Right Click on `test.whitebox` package and follow `Run As > JUnit test`

### White Box Test coverage

To perform white box testing Right Click on `test.whitebox` package and follow `Coverage As > JUnit test`

---

## Black-Box Testing

```
NOTE: Because of the way our application is designed and behaves, it requires some input to be entered in console for excution.
To achieve that in black-box testing, follow instructions given in Utility.java before running the tests 
```
### Running Black Box Testing

To perform black box testing Right Click on `test.blackbox` package and follow `Run As > JUnit test`

### Black Box Test coverage

To perform black box testing Right Click on `test.blackbox` package and follow `Coverage As > JUnit test`

---

## Running Application

To run and test the application, if you have previously followed the steps in `Utility.java` to run the blackbox tests, you need to undo these steps. Otherwise, the console will receive random string values instead of user inputs.

To run the application Right click on `Main.java` and do `Run As > Java Application`

---
&copy; Group 15
Authors (Amisha Rastogi, Ansar Patil, Emir Yücel, Mayur Shinde, Soumik Datta)