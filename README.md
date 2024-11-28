# TestMonitor API Java Client

This client provides a convenient Java wrapper for the [TestMonitor API](https://docs.testmonitor.com/).

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Changelog](#changelog)
- [Contributing](#contributing)
- [Credits](#credits)
- [License](#license)

## Installation

Before you start, make sure you have a [recent Java SDK](https://www.oracle.com/java/technologies/downloads/) installed.

### Maven Installation

We recommend installing the API client in your Java project using Maven. Add it as a dependency to your Maven configuration:

```xml
<dependency>
    <groupId>com.testmonitor</groupId>
    <artifactId>api-java-client</artifactId>
    <version>2.0</version>
</dependency>
```

### Manual Installation

If you prefer a standalone JAR library, checkout this repository and run Maven to compile the sources and generate a JAR file:

```sh
    $ git checkout https://github.com/testmonitor/api-java-client.git
    $ cd api-java-client
    $ mvn package
```

Your JAR file will be available in the `target` directory.

## Usage

If you haven't done so, make sure to create an **API token** in TestMonitor.

Start with a new client instance by providing your TestMonitor domain and API token:

```java
Client client = new Client("example.testmonitor.com", "t0ps3cr3t")
```

Now you can start interacting with TestMonitor.

To get a list of projects, use the following code:

```java
ArrayList<Project> projects = client.projects().list();
```

Use this code to create a new test case folder and test case (for project ID 1):

```java
Project project = client.projects().get(1);

TestCaseFolder testCaseFolder = client.testCaseFolders(project).create("A Test Case Folder");
TestCase testCase = client.testCases(project).create("A Test Case", testCaseFolder);
```

Full documentation is available at our [Wiki](https://github.com/testmonitor/api-java-client/wiki).

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
