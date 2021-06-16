# Bazel Fundamentals Workshop - Building Simple Android Application

In this workshop you will be developing a simple Bazel Android application to get familiar with the Bazel build tool basics.

The application will consist of the main Android module, Kotlin library and additional Android library. By the end of this workshop you will have an understanding about how to build and run Kotlin Android applications and unit tests for them using Bazel.

In order to follow the tutorial, open `/start` folder and consider it as a **root** directory of your future Bazel project.

You can also check you progress with the completed application in `/completed` directory.

For any information about Bazel build tool please refer to the [official page](https://www.bazel.build/).

## Table of contents
- [Pre-requisites](#pre-requisites)
- [Environment setup](#environment-setup)
    - [Bazelisk](#bazelisk)
    - [Android Studio / IntelliJ IDEA plugin (Optional)](#android-studio-intellij-idea-plugin-optional)
    - [Visual Studio Code plugin (Optional)](#visual-studio-code-plugin-optional)
    - [Buildifier (Optional)](#buildifier-optional)
- [Creating Bazel Android app](#creating-bazel-android-app)
    - [Step `1`. Create Bazel `WORKSPACE`](#step-1-create-bazel-workspace)
    - [Step `2`. Create `BUILD` target](#step-2-create-build-target)
    - [Step `3`. Launch the app](#step-3-launch-the-app)
    - [Step `4`. Add Kotlin library](#step-4-add-kotlin-library)
    - [Step `5`. Add external dependency](#step-5-add-external-dependency)
    - [Step `6`. Run tests](#step-6-run-tests)
- [Complete by yourself](#complete-by-yourself)
    - [Step `7`. Add Android library](#step-7-add-android-library)
- [Conclusion](#conclusion)
- [Further learning](#further-learning)
- [Useful references](#useful-references)

## Pre-requisites
In order to proceed with this workshop you need to have:
- Installed [Android Studio](https://developer.android.com/studio) or standalone Android SDK.
- Your favourite IDE or Text Editor. (It is preferred to use [Android Studio](https://developer.android.com/studio), [IntelliJ IDEA Community](https://www.jetbrains.com/idea/download/other.html) or [Visual Studio Code](https://code.visualstudio.com/)).
> **Note:** As of now IntelliJ IDEA version 2020.2 do not support Bazel plugin, so it is prefered to use 2020.1.4 for this workshop.
- Installed JDK 8. 
> **Note:** If you decided to use IntelliJ IDEA or Android Studio you can use bundled JDK.
- Minimal experience with Java, Kotlin and Android development.
> **Note:** Do not worry if you don't have much experience with any of the above technologies. You still will be able to understand Bazel basics and follow the workshop steps as we will not get into platform/language details too much. Just make sure all the required software is installed before starting. 
- Installed [Homebrew](https://brew.sh/).
- Motivation to use Bazel as a build tool for your project.

<br>

## Environment setup
In order to get your machine ready to work with Bazel, follow the instructions below:

### Bazelisk
[Official page](https://github.com/bazelbuild/bazelisk)

_Bazelisk is a wrapper for Bazel written in Go. It automatically picks a good version of Bazel given your current working directory, downloads it from the official server (if required) and then transparently passes through all command-line arguments to the real Bazel binary. You can call it just like you would call Bazel._

If you don't have [Homebrew](https://brew.sh/) on your Mac/Linux machine install it:
```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install.sh)"
```

Install bazelisk with Homebrew:
```bash
$ brew install bazelisk
```
Verify that Bazel is installed on your machine:
```bash
$ bazel --version
```

>**Note:** Check other installation options on the [official documentation page](https://docs.bazel.build/versions/master/install.html).

### <a id="android-studio-intellij-idea-plugin-optional"></a> Android Studio / IntelliJ IDEA plugin (Optional)

[Official page](https://plugins.jetbrains.com/plugin/9185-bazel)

To have full IDE experience with code completion and other good stuff you can use the official Bazel plugin for Android Studio / IntelliJ IDEA.

<br>

To install plugin select...

`Android Studio -> Preferences -> Plugins -> Marketplace` 

... and search for `Bazel` plugin developed by Google.

<br>

After you installed the plugin open...

`Android Studio -> Preferences -> Bazel Settings`

... and make sure `Bazel binary location` is set as `/usr/local/bin/bazelisk`

<br>

Now you are ready to import your Bazel project. To do this select...

`File -> Inport Bazel Project...`

... set the **root** directory of your project and follow further instructions.

> **Note:** Root directory of you project must contain the `WORKSPACE` file which will be described below. 

<br>

After the project is imported you can find the `Bazel` menu option in the app menu bar. 

![App menu bar](img1.png)

To sync the project files select...


`Bazel -> Sync -> Sync Project with BUILD files`


#### Troubleshooting:
**Issue:** 

_Bazel plugin does not recognize java/kotlin source sets and fails to sync java/kotlin files considering their package to be wrong._ 

_E.g. it expects `src/main/kotlin/com/morfly/bazel` package instead of `com/morfly/bazel`._ 

**Solution:**
- Make sure `BUILD` file is placed in the same package as your java/kotlin source code. If BUILD file is located in another location you should explicitly set the `custom_package` attribute.
- Make sure, java/kotlin files has corect `package` set.
- If it is an Android module make sure `AndroidManifest.xml` has correct `package` attrubute set. 
- If all the above does not solve the issue, uninstall Bazel plugin and install it again. After restarting the IDE it should work correct. 


### <a id="visual-studio-code-plugin-optional"></a> Visual Studio Code plugin (Optional)

[Official page](https://marketplace.visualstudio.com/items?itemName=BazelBuild.vscode-bazel)

Install official plugin from the Visual Studio Marketplace to enable Bazel VS Code support such as code highlighting and completion. 


### <a id="buildifier-optional"></a> Buildifier (Optional)
[Official page](https://github.com/bazelbuild/buildtools/blob/master/buildifier/README.md)

_Buildifier is a tool for formatting bazel BUILD and .bzl files with a standard convention._

```bash
$ brew install buildifier
```

_In order to make buildifier automatically find all Starlark files (i.e. BUILD, WORKSPACE, .bzl, or .sky) in a directory recursively:_
```bash
$ buildifier -r {your_local_path}/bazel-tutorial/start
```

<br>

## Creating Bazel Android app
Now when you machine is ready to work let's build an Android Bazel app.

Refer to the following repository structure to locate files required for this workshop:
```
bazel-tutorial
|
└── start  <- working directory for this workshop
|
└── competed
```

Open `{your_local_path}/bazel-tutorial/start` project with your favorite IDE / Text Editor.

To check your progress use `completed` project directory which contains the resulting source code for this workshop.

### <a id="step-1-create-bazel-workspace"></a> Step 1. Create Bazel `WORKSPACE`
_A workspace is a directory on your filesystem that contains the source files for the software you want to build, as well as symbolic links to directories that contain the build outputs. Each workspace directory has a text file named WORKSPACE which may be empty, or may contain references to external dependencies required to build the outputs._

_Directories containing a file called WORKSPACE are considered the root of a workspace. Therefore, Bazel ignores any directory trees in a workspace rooted at a subdirectory containing a WORKSPACE file (as they form another workspace)._

<br>

Create a `WORPSPACE` file in the **root** project directory:

`{your_local_path}/bazel-tutorial/start/WORKSPACE`

> **Note:** It is possible to name the file as `WORKSPACE.bazel` as well. Bazel will consider these two options equally.

Open terminal at the **root** project directory and type:

```bash
$ bazel info workspace
```
If everything was done right you will see the the path to your project **root** directory as an output.

In order to include Android SDK to your project add this code to your `WORKSPACE` file:
```python
# WORKSPACE

android_sdk_repository(
    name = "androidsdk"
)
```
By default Bazel will use `$ANDROID_HOME` to access Android SDK. 
Check if you `ANDROID_HOME` is set on your machine:
```bash
$ echo $ANDROID_HOME
```
If for some reason you want to explicitly specify path to Android SDK you can set it in the `WORKSPACE` file.

Usually Android SDK is stored at the following location: `/Users/{username}/Library/Android/sdk`

```python
# WORKSPACE

android_sdk_repository(
    name = "androidsdk",
    path = "Users/illidan.stormrage/Library/Android/sdk", # new code
)
```

It is also possible to set specific version of Android SDK to be used in your project:

```python
# WORKSPACE

android_sdk_repository(
    name = "androidsdk",
    path = "Users/bruce.wayne/Library/Android/sdk`",
    api_level = 29,                # new code
    build_tools_version = "29.0.3" # new code
)
```

#### Results
Here is our progress so far:
```
start
└── WORKSPACE  <-- created
```

### <a id="step-2-create-build-target"></a> Step 2. Create `BUILD` target
Now, when the initial project workspace setup is done, we can define buildable targets. To do this we need to use `BUILD.bazel` files.

_BUILD files tell Bazel how to build different parts of the project. (A directory within the workspace that contains a BUILD file is a package.)_

Open `src/app/java/com/morfly/bazel` directory of the project. As you can see it contains Java Android source code including `AndroidManifest.xml` and `res` directory.

In order to build and run this code we need to create a `BUILD.bazel` file and create a target which in turn is an instance of Bazel rule:

Create a build file at the following location: `src/app/java/com/morfly/bazel/BUILD.bazel` and create a target by using `android_library` rule as shown below:

```python
# src/app/java/com/morfly/bazel/BUILD.bazel

android_library(
    name = "main_screen",
    srcs = glob([
        "MainActivity.java",
        "Utils.java"
    ]),
    manifest = "AndroidManifest.xml",
    resource_files = glob([
        "res/**"
    ]),
)
```

As you can see, we have specified source files which should be included to the target, manifest file and resource files which include layout, strings, colors, etc.

Also, note that every target should have a name. Other targets will be able to depend on this one by refering to its name `:main_screen`.

> **Note:** Bazel equally considers `BUILD` and `BUILD.bazel` files. However, it is better to use latter in case of migration from Gradle project as Gradle generates `build` directories that can conflict with Bazel `BUILD` files.

We have created an Android library target but we are not ready to build the project yet.

In order to generate `.apk` file it is required to use `android_binary` rule.

Create another build file at the following location: `src/app/BUILD.bazel` and create a target by using `android_binary` rule:
```python
# src/app/BUILD.bazel

android_binary(
    name = "app",
    manifest = "AndroidManifest.xml",
    deps = [
        "//src/app/java/com/morfly/bazel:main_screen",
    ],
)
```
As you can see, we are referencing previously created `:main_screen` target.

Such reference format is called **label** in Bazel, where: 
- `//` specifies that we are referencing the target from **root** of the workspace.
- `src/app/java/com/morfly/bazel` specifies the package name. (_Please, note that Bazel package and java package are not related to each other. They are different concepts._)
- `:main_screen` specifies the target name.

> **Note:** It is possible to use shorter labels in some specific cases. Plese refer to the [official documentation page](https://docs.bazel.build/versions/master/build-ref.html#labels) to learn more.

Now we are ready to build the project. To do this use the following command:

```bash
$ bazel build //src/app:app
```
Or, since `:app` target has the same name as its directory, we can use the shortened label: 
```bash
$ bazel build //src/app
```
If you run the command above you will see the following error:

```
ERROR: {your_local_path}/bazel-tutorial/completed/src/app/BUILD.bazel:1:15: 
in android_binary rule //src/app:app: target '//src/app/java/com/morfly/bazel:main_screen' 
is not visible from target '//src/app:app'. 
Check the visibility declaration of the former target if you think the dependency is legitimate
ERROR: Analysis of target '//src/app:app' failed; build aborted: Analysis of target '//src/app:app' failed
INFO: Elapsed time: 0.138s
INFO: 0 processes.
FAILED: Build did NOT complete successfully (1 packages loaded, 7 targets configured)
```

To resolve this issue we need to get familiar with the _visibility_ concept.

_Visibility controls whether a target can be used (depended on) by targets in other packages. This helps other people distinguish between your library’s public API and its implementation details, and is an important tool to help enforce structure as your workspace grows._

Please, refer the [official documentation page](https://docs.bazel.build/versions/master/visibility.html) to learn more.

In order to make `:main_screen` target available for the `:app` add the following line at the beginning of `src/app/java/com/morfly/bazel/BUILD.bazel` file:

```python
# src/app/java/com/morfly/bazel/BUILD.bazel

package(default_visibility = ["//src:__subpackages__"]) # new code                                                  

android_library(
    name = "main_screen",
    ...
)
```
By this we are telling that **any** package under `//src` may access `:main_screen` target.

Run the build again:

```bash
$ bazel build //src/app
```

Now the build should succeed.

> **Note:** All the code in Bazel build files such as `WORKSPACE`, `*.bazel`, `*.bzl` is written in Starlark language which is a simplified version of Python 3. So, don't be confused when you see more Bazel code in further sections. It is just plain old Python.

#### Results
Here is out progress so far:
```
start
│
└── src/app
    └── BUILD.bazel  <-- added :main_screen target (android_library rule)
    │
    └── java/com/morfly/bazel
        └── BUILD.bazel  <-- added :app target (android_binary rule)
```

### <a id="step-3-launch-the-app"></a> Step 3. Launch the app
To install the app on the mobile phone or emulator use this command:
```bash
$ bazel mobile-install //src/app --start_app
```
>**Note:** By default Bazel will not run the installed application.
To do this use `--start_app` parameter.

> **Note:** Make sure that you have launched Android emulator prior to performing the command above.

Refer to the [official documentation page](https://docs.bazel.build/versions/master/mobile-install.html) to learn more.
### <a id="step-4-add-kotlin-library"></a> Step 4. Add Kotlin library
So far we have used only java in this workshop. 
To be able to use kotlin in our project we need make it available for us.

To do this, add the following code to the end of `WORKSPACE` file:

```python
# WORKSPACE

...

# new code v
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

rules_kotlin_version = "legacy-1.3.0"

rules_kotlin_sha = "4fd769fb0db5d3c6240df8a9500515775101964eebdf85a3f9f0511130885fde"

http_archive(
    name = "io_bazel_rules_kotlin",
    sha256 = rules_kotlin_sha,
    strip_prefix = "rules_kotlin-%s" % rules_kotlin_version,
    type = "zip",
    urls = ["https://github.com/bazelbuild/rules_kotlin/archive/%s.zip" % rules_kotlin_version],
)

load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kotlin_repositories", "kt_register_toolchains")

kotlin_repositories()

kt_register_toolchains()
# new code ^
```

Now we are able to create Kotlin targets. In this workshop we will create a kotlin library which will be added as a dependency to the `:main_screen` target.

To create a Kotlin target we need to use `kt_jvm_library` rule.

Create a `BUILD` file at the following location: `src/jvmlib/kotlin/com/morfly/bazel/BUILD.bazel` and paste there the following code:
```python
# src/jvmlib/kotlіn/com/morfly/bazel/BUILD.bazel

load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

package(default_visibility = ["//src:__subpackages__"])

kt_jvm_library(
    name = "kotlin_library",
    srcs = glob([
        "*.kt"
    ]),
)
```
Note that in order to use `kt_jvm_library` rule we need to `load` it to the build file.
You can consider `load` as an import statement where:
- `@io_bazel_rules_kotlin` refers to another (external) workspace `io_bazel_rules_kotlin`.
- `//kotlin` stands for a package name.
- `kotlin.bzl` stands for a file where our rule is defined.
- `kt_jvm_library` is a specific rule that we want to import.
> **Note:** `.bzl` are extension files where it is possible to define custom macros and rules in Bazel. Extensions are out of scope of the current workshop, so please refer to the [official documentation page](https://docs.bazel.build/versions/master/skylark/concepts.html) for more information.

In order to be able to use code from `:kotlin_library` in `:main_screen` we need to add former one as a dependency to latter one.
To do this open the `BUILD` file where `:main_screen` is defined and add the following line to `deps` attribute:
```python
# src/app/com/morfly/bazel/BUILD.bazel

android_library(
    name = "main_screen",
    ...
    deps = [
        "//src/jvmlib/kotlin/com/morfly/bazel:kotlin_library",    # new code
    ]
)
```

Open `src/app/java/com/morfly/bazel/MainActivity.java` file and uncommend the following code:
```java
// src/app/java/com/morfly/bazel/MainActivity.java

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ...

        // updated code, lines 20-24
        Library library = new KotlinLibrary();

        activateVersionButton.setOnClickListener(v ->
                libraryVersionTextView.setText(Utils.formattedLibraryDescription(library))
        );
        // updated code ^
    }
}
```
> **Note:** Do not forget about imports.

Open src/app/java/com/morfly/bazel/Utils.java file and uncomment `formattedLibraryDescription` function:
```java
public class Utils {

    // updated code, lines 6-8
    public static String formattedLibraryDescription(Library library) {
        return "About library:\n" + library.getAbout();
    }
    // updated code ^
}
```

Now you can build and run the app.

#### Results
Here is out progress so far:

```
start
└── WORKSPACE  <-- added Kotlin language
│
└── src
    │
    └── app/java/com/morfly/bazel
    │   └── BUILD.bazel  <-- updated :main_screen target
    │
    └── jvmlib/kotlіn/com/morfly/bazel
        └── BUILD.bazel  <-- added :kotlіn_library target (kt_jvm_library rule)
```

### <a id="step-5-add-external-dependency"></a> Step 5. Add external dependency
Android, Java and Kotlin communities have reach set tools and libraries which can be extremely helpful during the development.

In this workshop we will use [ConstraintLayout](https://developer.android.com/reference/androidx/constraintlayout/widget/ConstraintLayout) to refactor our UI code in `:main_screen` target.

Just like in Gradle, it is possible to add external dependencies to Bazel projects. To add such dependency to our project paste this code at the end of WORKSPACE file:

```python
# WORKSPACE

...

# new code v
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

RULES_JVM_EXTERNAL_TAG = "3.3"
RULES_JVM_EXTERNAL_SHA = "d85951a92c0908c80bd8551002d66cb23c3434409c814179c0ff026b53544dab"

http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    sha256 = RULES_JVM_EXTERNAL_SHA,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
       "androidx.constraintlayout:constraintlayout:2.0.0",
    ],
    repositories = [
        "https://jcenter.bintray.com/",
        "https://maven.google.com",
        "https://repo1.maven.org/maven2",
    ],
)
# new code ^
```
So, what is happening in the code above?
- First, we need to add `rules_jvm_external` to our project.
- `@rules_jvm_external//:defs.bzl` extension contains the rule `maven_install` which will help us to fetch external maven packages to our project.
- Finally, we specify artifacts which should be added to our project using `maven_install` rule.
 
Now, when external dependency became available in our project we need to specify which targets should use them. To do this, we need to update `:main_screen` target definition:

```python
# src/app/com/morfly/bazel/BUILD.bazel

android_library(
    name = "main_screen",
    ...
    deps = [
        "//src/jvmlib/kotlin/com/morfly/bazel:kotlin_library",
        "@maven//:androidx_constraintlayout_constraintlayout", # new code
    ]
)
```

Now, we can use `ConstraintLayout` in our code. Follow the steps below:
1. Open `src/app/com/morfly/bazel/res/layout/activity_main_v2.txt` and **rename** it to `activity_main_v2.xml`.
1. Use `activity_main_v2.xml` in `MainActivity.java`:
```java
// src/app/java/com/morfly/bazel/MainActivity.java

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ...

        setContentView(R.layout.activity_main_v2); // updated code, line 15

        ...
    }
}
``` 

Build and run the `:app` target.

#### Results
Here is out progress so far:

```
start
└── WORKSPACE  <-- added ConstraintLayout dependency
│
└── src/app/java/com/morfly/bazel
    └── BUILD.bazel  <-- updated :main_screen target
```

### <a id="step-6-run-tests"></a> Step 6. Run tests
Now, when we learned how to build and run our Bazel app, we need to figure out how to test our code.

> **Note:** In this workshop we will consider only unit-tests. If you want to learn more about Android instrumentation tests refer to the [official documentation page](https://docs.bazel.build/versions/master/android-instrumentation-test.html#:~:text=For%20hermeticity%20and%20reproducibility%2C%20Bazel,without%20passing%20states%20between%20them.).

In order to write unit-tests for our mobile app we will be using JUnit 4.
If you remember previous steps, we need to add JUnit 4 artifact to our project. In order to do this open your `WORKSPACE` file and update `maven_install` section:
```python
# WORKSPACE

...

maven_install(
    artifacts = [
        "androidx.constraintlayout:constraintlayout:2.0.0",
        "junit:junit:4.12",    # new code
    ],
    repositories = [
        ...
    ],
)

```
Now we can use JUnit to writing our unit-tests.

Out unit-test code is located in `src/javatests/com/morfly/bazel/UtilsTest.java` file.

To actually run those tests we need to create a target using `java_test` rule. To do this create file "//src/app/javatests/com/morfly/bazel/BUILD.bazel and paste there the following code:
```python
# src/app/tavatests/com/morfly/bazel/BUILD.bazel 

java_test(
    name = "main_unit_tests",
    test_class = "com.morfly.bazel.UtilsTest",
    srcs = [
        "UtilsTest.java"
    ],
    deps = [
        "//src/app/java/com/morfly/bazel:main_screen",
        "//src/jvmlib/kotlin/com/morfly/bazel:kotlin_library",
    ]
)
```
As you can see, we have created `:main_unit_tests` target, specified `test_class` and `srcs` attributes and added `:main_screen` and `:kotlin_library` targets as dependencies to our test target.


To run our unit-tests use the following command:
```bash
$ bazel test //src/app/javatests/com/morfly/bazel:main_unit_tests --test_output=all
```

>**Note:** By default Bazel will not output test results to console. Logs can be found in the `bazel-testlogs/test.log` file. In order to print test results to console use `--test_output=all` parameter.

#### Results
Here is out progress so far:

```
start
└── WORKSPACE  <-- added JUnit dependency
│
└── src/app/tavatests/com/morfly/bazel
    └── BUILD.bazel  <-- added :main_unit_tests target (java_test rule)
```

<br>

## Complete by yourself

During this workshop you have discovered how to build, run and test Android Bazel applications. 

Now try to practice and implement new functionallity by yourself.
### <a id="step-7-add-android-library"></a> Step 7. Add Android library

We need to add Android library to the project and show information about it on the main screen instead of Kotlin library which we have already integrated.

**TODO:**
1. Open `src/androidlib/kotlin/com/morfly/bazel` directory.
1. Create `//src/androidlib/kotlin/com/morfly/bazel:android-library` target which includes `AndroidLibrary.kt` source file.
> **Hint:** You can use a `kt_android_library` rule imported from `@bazel_tools//tools/build_defs/repo:http.bzl`.
3. Created `:android-library` target should also depend on `:kotlin-library`.
1. Add `:android-library` target as a dependency to the `:main_screen`.
1. Modify `MainActivity.java` file:
```java
// src/app/java/com/morfly/bazel/MainActivity.java

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ...

        Button showVersionButton = ...;
        TextView libraryVersionTextView = ...;

        Library library = new AndroidLibrary(this); // updated code, line 20

        activateVersionButton.setOnClickListener(v -> ... );
    }
}
```
>**Note:** Do not forget about imports.

6. Build and run `:app` target.
1. Press the `About library` button. You should see: `com.morfly.bazel: Android library. Version 0.1.0` message on your emulator/device screen.

<br>

## Conclusion

In this workshop we have have done the following:
- Developed Bazel Android multimodule application with using Java and Kotlin languages.
- Got familiar with Bazel build specifics by using `WORKSPACE` and `BUILD.bazel` files.
- Included 3rd-party dependency to our Bazel project.
- Discovered how to run unit-tests with Bazel.

Here is the Bazel file structure that we have built:
```
start
└── WORKSPACE
│
└── src
    │
    └── app
    │   └── BUILD.bazel  <-- :app
    │   │
    │   └── java/com/morfly/bazel
    │   │   └── BUILD.bazel  <-- :main_screen
    │   │
    │   └── javatests/com/morfly/bazel
    │       └── BUILD.bazel  <-- :main_unit_tests
    │
    └── jvmlib/kotlіn/com/morfly/bazel
    │   └── BUILD.bazel  <-- :kotlіn_library
    │
    └── androidlib/kotlіn/com/morfly/bazel
        └── BUILD.bazel  <-- :android_library
```

Congratulations! At this point you can start building your own Bazel applications. Of course, there are more advanced topics related to Bazel which will allow you to go beyond basic use cases, so feel free to check out [further learning](#further-learning) and [useful references](#refs) sections for hits about what to learn next. 

Thank you for going through this workshop and have a great learning!

Your feedback and contribution is most welcome here!

<br>

## Further learning
- Bazel extensions:
    - Starlark language and `.bzl` files.
    - Macros.
    - Rules.
- Queries.
- Android instrumentation tests.

<br>

## Useful References
- [Official Bazel documentation:](https://docs.bazel.build/versions/3.5.0/bazel-overview.html)
    - [Java tutorial](https://docs.bazel.build/versions/master/tutorial/java.html).
    - [Android tutorial](https://docs.bazel.build/versions/master/tutorial/android-app.html).
    - [Creating a macro](https://docs.bazel.build/versions/master/skylark/tutorial-creating-a-macro.html).
    - [Rules tutorial](https://docs.bazel.build/versions/master/skylark/rules-tutorial.html).
- [Official examples Git repository](https://github.com/bazelbuild/examples).
- BazelCon conference:
    - Half-day Bazel bootcamp: [part 1](https://www.youtube.com/watch?v=BGOEq5FdNUQ) and [part 2](https://www.youtube.com/watch?v=1KbfkOWO-DY).
    - [2017 playlist](https://conf.bazel.build/2017/session-videos).
    - [2018 playlist](https://conf.bazel.build/2018/session-videos).
    - [2019 playlist](https://www.youtube.com/playlist?list=PLxNYxgaZ8Rsf-7g43Z8LyXct9ax6egdSj).
    - [2020 playlist](https://opensourcelive.withgoogle.com/events/bazelcon2020)
- [Benjamin Mushko, 2020, _Getting Started with Bazel_, O'Relly](https://get.oreilly.com/ind_getting-started-with-bazel.html).
- Series of articles [_Writing Bazel rules_](https://jayconrod.com/posts/106/writing-bazel-rules--simple-binary-rule) on _jayconrod.com_.
- [Building Android Apps with Bazel](https://codelabs.developers.google.com/codelabs/bazel-android-intro/index.html#0) official codelab.

<br>

---
_Author: Pavlo Stavytskyi_
