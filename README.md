# TestMonitor API Java Client

This client provides a convenient Java wrapper for the [TestMonitor API](https://docs.testmonitor.com/).

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Examples](#examples)
- [Changelog](#changelog)
- [Contributing](#contributing)
- [Credits](#credits)
- [License](#license)

## Installation

To install the client you need to include it in your Maven configuration:

```
<dependency>
    <groupId>com.testmonitor</groupId>
    <artifactId>api-java-client</artifactId>
    <version>1.0</version>
</dependency>
```

## Usage

Before you start, make sure you have created an API token in TestMonitor.

Instantiate a new client using your TestMonitor domain and API token:

```java
Client client = new Client("example.testmonitor.com", "t0ps3cr3t")
```

## Examples

Here are some code examples to get you started.

### Users

To get a list of TestMonitor users, use the following code fragment:

```java
ArrayList<User> users = client.users().list();
```

This will return a paginated list of users, limited by the default maximum of 15 records.

You can change the page and the record limit by simply passing them as arguments:

```java
ArrayList<User> users = client.users().list(2, 30);
```

Sometimes you want to know which user is logged in. Therefore you can use the `currentUser` method:

```java
User user = client.users().currentUser();
```

### Projects

To get a list of TestMonitor projects, use the following code fragment:

```java
ArrayList<Project> projects = client.projects().list();
```

This will return a paginated list of projects, limited by the default maximum of 15 records.

You can change the page and the record limit by simply passing them as arguments:

```java
ArrayList<Project> projects = client.projects().list(2, 30);
```

This will retrieve the second page of a list of 30 records, i.e., records 31 through 60.

To get a single TestMonitor project, use this code:

```java
Project project = client.projects().get(1);
```

You can access its properties by using its getters:

```java
Project project = client.projects().get(1);

System.out.println(project.getName());
System.out.println(project.getStartDate().toString());
System.out.println(project.getEndDate().toString());
```

This will output the project name, start- and end date.

Updating a project is just a matter of using the setters and the update method:

```java
Project project = client.projects().get(1);

project.setName("A New Name");

client.projects().update(project);
```

A project is needed for most of the endpoints in this API.

### Test Suites
In order to work with test suites, make sure you have created a project first. Make a note of its project ID: you are going to
need this from this point forward.

To get a list of test suites, use this code:

```java
Project project = client.projects().get(1);
ArrayList<TestSuite> testSuite = client.testSuites(project).list();
```

This will get a paginated list of test suites for a project with ID 1.

When it is needed to browse to a list of test suites, it is possible to enter a page number, for example we want to see the third page:

```java
ArrayList<TestSuite> testSuite = client.testSuites(project).list(3);
```

At last you may want to change the pager limit. For example you want to view the fourth page with a pager limit of 10:

```java
ArrayList<TestSuite> testSuite = client.testSuites(project).list(4, 10);
```

Using the `findOrCreate` method, you can locate a test suite and if it doesn't exists, the client will create it for you. This is
especially handy when your script relies on the presence of a test suite, but you want to avoid checking for its existence every time
your script is run.

The `findOrCreate` method uses the test suite name in order to determine its presence:

```java
Project project = client.projects().get(1);
TestSuite testSuite = client.testSuites(project).findOrCreate("A Test Suite"));
```

This will look for a test suite named "A Test Suite"; when it is present, it will return the test suite object. Otherwise, it will create
the test suite and return it as an object.

Creating a test suite is simple by passing the name of the test suite to the create method. Be aware that this always create a new test suites, even when there is a duplicate name exists in the project. 
If you want to avoid duplicates or want to use a previous created test suite, you may want to consider `findOrCreate`.

```java
TestSuite testSuite = client.testSuites(project).create("A Test Suite"));
```

Updating a test suite is just a matter of using the setters and the update method:

```java
TestSuite testSuite = client.testSuites(project).get(1);

testSuite.setName("A New Name");

client.testSuites(project).update(testSuite);
```

### Test Cases
To get a list of test cases, use this code:

```java
Project project = client.projects().get(1);

ArrayList<TestCase> testCase = client.testCases(project).list();
```

This will get a paginated list of test cases for a project with ID 1.

When it is needed to browse to a list of test cases, it is possible to enter a page number, for example we want to see the third page:

```java
ArrayList<TestCase> testCase = client.testCases(project).list(3);
```

At last you may want to change the pager limit. For example you want to view the fourth page with a pager limit of 10:

```java
ArrayList<TestCase> testCase = client.testCases(project).list(4, 10);
```

Using the `findOrCreate` method, you can locate a test case and if it doesn't exists, the client will create it for you. This is
especially handy when your script relies on the presence of a test case, but you want to avoid checking for its existence every time
your script is run.

The `findOrCreate` method uses the test case name in order to determine its presence:

```java
Project project = client.projects().get(1);

TestSuite testSuite = client.testSuites(project).get(1);

TestCase testCase = client.testCases(project).findOrCreate("A Test Case", testSuite));
```

This will look for a test case named "A Test Suite"; when it is present, it will return the test case object. Otherwise, it will create
the test case and return it as an object.

Creating a test case is simple by passing the name of the test case to the create method. Be aware that this always create a new test cases, even when there is a duplicate name exists in the project.
If you want to avoid duplicates or want to use a previous created test case, you may want to consider `findOrCreate`.

```java
TestSuite testSuite = client.testSuites().get(1);

TestCase testCase = client.testCases(project).create("A Test Case", testSuite));
```

Updating a test case is just a matter of using the setters and the update method:

```java
TestCase testCase = client.testCases(project).get(1);

testCase.setName("A New Name");

client.testCases(project).update(testCase);
```

### Milestones
To get a list of milestones, use this code:

```java
Project project = client.projects().get(1);

ArrayList<Milestone> milestones = client.milestones(project).list();
```

This will get a paginated list of milestones for a project with ID 1.

Using the `findOrCreate` method, you can locate a milestone and if it doesn't exists, the client will create it for you. This is
especially handy when your script relies on the presence of a milestone, but you want to avoid checking for its existence every time
your script is run.

The `findOrCreate` method uses the milestone name in order to determine its presence:

```java
Project project = client.projects().get(1);

Milestone milestone = client.milestones(project).findOrCreate("A Milestone"));
```

This will look for a milestone named "A Milestone"; when it is present, it will return the milestone object. Otherwise, it will create 
the milestone and return it as an object.

### Test Runs
To get a list of test runs, use this code:

```java
Project project = client.projects().get(1);

ArrayList<TestRun> testRun = client.testRuns(project).list();
```

This will get a paginated list of test runs for a project with ID 1.

When it is needed to browse to a list of test runs, it is possible to enter a page number, for example we want to see the third page:

```java
ArrayList<TestRun> testRun = client.testRuns(project).list(3);
```

At last you may want to change the pager limit. For example you want to view the fourth page with a pager limit of 10:

```java
ArrayList<TestRun> testRun = client.testRuns(project).list(4, 10);
```

Using the `findOrCreate` method, you can locate a test run and if it doesn't exists, the client will create it for you. This is
especially handy when your script relies on the presence of a test run, but you want to avoid checking for its existence every time
your script is run.

The `findOrCreate` method uses the test run name in order to determine its presence:

```java
Project project = client.projects().get(1);

Milestone milestone = client.milestones().get(1);

TestRun testRun = client.testRuns(project).findOrCreate("A Test Case", milestone));
```

This will look for a test run named "A Test Suite"; when it is present, it will return the test run object. Otherwise, it will create
the test run and return it as an object.

Creating a test run is simple by passing the name of the test run to the create method. Be aware that this always create a new test runs, even when there is a duplicate name exists in the project.
If you want to avoid duplicates or want to use a previous created test run, you may want to consider `findOrCreate`.

```java
Milestone milestone = client.milestones().get(1);

TestRun testRun = client.testRuns(project).create("A Test Case", milestone));
```

Updating a test run is just a matter of using the setters and the update method:

```java
TestRun testRun = client.testRuns(project).get(1);

testRun.setName("A New Name");

client.testRuns(project).update(testRun);
```

Adding users to a test run be done by calling the `assignUsers` method. You have to pass the test run and a list of user id's. Below is an example to add yourself to a test run:

```java
List<Integer> users = new ArrayList<>();

users.add(client.users().currentUser().getId());

client.testRuns(project).assignUsers(testRun, users);
```

Adding test cases to a test run be done by calling the `assignTestCases` method. You have to pass the test run and a list of test cases id's:

```java
List<Integer> testCases = new ArrayList<>();

testCases.add(1);

testCases.add(myTestCase.getId());

client.testRuns(project).assignTestCases(testRun, testCases);
```

### Test Results

Creating a test result can be done as follows:

```java
TestResult testResult = new TestResult();

testResult
    .setDescription("This is what happend!")
    .setTestResultCategoryId(TestResultCategory.PASSED)
    .setTestRunId(testRun.getId())
    .setTestCaseId(testCase.getId())
    .setDraft(false);

client.testResults(project).create(testResult);
```

The test result category is an integer that correspondent with the categories (smileys) in TestMonitor. There are four possible values, we put in a constant to help you a bit:

```java
TestResultCategory.PASSED
TestResultCategory.CAUTION
TestResultCategory.FAILED
TestResultCategory.BLOCKING
```

Be aware that the `draft` state determines that the test result is counting in the reports. A test result that is on draft is considdert 'incomplete'. The default is `true`. 

Sometimes you want to add an attachment to a test result. Therefore is the `addAttachment` method:

```java
File file = new File(); // See the JAVA documentation about to create or read a file object.
        
client.testResults(this.project).addAttachment(testResult, file);
```

Some clarification about the above:
- A test result is always created by the current user (it is not possible the spoof another user over the API).
- You should first create a test result (call the API and use that result) before you add an attachment to it.
- In some cases you need to change some settings in TestMonitor so you can create test results without adding an attachment. See therefore our knowledge base. 

## Changelog

Refer to [CHANGELOG](CHANGELOG.md) for more information.

## Contributing

Refer to [CONTRIBUTING](CONTRIBUTING.md) for contributing details.

## Credits

* **Thijs Kok** - *Lead developer* - [ThijsKok](https://github.com/thijskok)
* **Stephan Grootveld** - *Developer* - [Stefanius](https://github.com/stefanius)
* **Frank Keulen** - *Developer* - [FrankIsGek](https://github.com/frankisgek)
* **Muriel Nooder** - *Developer* - [ThaNoodle](https://github.com/thanoodle)

## License

The MIT License (MIT). Refer to the [License](LICENSE.md) for more information.
