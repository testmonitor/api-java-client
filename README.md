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
```

## Usage

Before you start, make sure you have created an API token in TestMonitor.

Instantiate a new client using your TestMonitor domain and API token:

```java
Client client = new Client("example.testmonitor.com", "t0ps3cr3t")
```

## Examples

Here are some code examples to get you started.

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

### Milestones

In order to work with milestones, make sure you have created a project first. Make a note of its project ID: you are going to
need this from this point forward.

To get a list of milestones, use this code:

```java
ArrayList<Milestone> milestones = client.milestones(1).list();
```

This will get a paginated list of milestones for a project with ID 1.

Using the `findOrCreate` method, you can locate a milestone and if it doesn't exists, the client will create it for you. This is
especially handy when your script relies on the presence of a milestone, but you want to avoid checking for its existence every time
your script is run.

The `findOrCreate` method uses the milestone name in order to determine its presence:

```java
Milestone milestone = client.milestones(1).findOrCreate("A Milestone", LocalDate.of(2022, 1, 1))
```

This will look for a milestone named "A Milestone"; when it is present, it will return the milestone object. Otherwise, it will create 
the milestone and return it as an object.

### Test Cases

### Test Suites

### Test Runs

### Test Results

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
