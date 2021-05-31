# Pre-requisites
In order to proceed with this workshop you need to have:
- Installed [Android Studio](https://developer.android.com/studio) or standalone [Android SDK](https://developer.android.com/studio).
- Your favourite IDE or Text Editor. (It is preferred to use [Android Studio](https://developer.android.com/studio), [IntelliJ IDEA Community](https://www.jetbrains.com/idea/download/other.html) or [Visual Studio Code](https://code.visualstudio.com/)).
> **Note:** As of now IntelliJ IDEA version 2020.2 do not support Bazel plugin, so it is prefered to use 2020.1.4 for this workshop.
- Installed [JDK 8](). 
> **Note:** If you decided to use IntelliJ IDEA or Android Studio you can use bundled JDK.
- Installed [Homebrew](https://brew.sh/) or installed [Bazelisk](https://github.com/bazelbuild/bazelisk).
- Cloned workshop repository.

<br>

## Install Bazelisk
Bazelisk is a Bazel wrapper which automatically handles versioning.

If you don't have Homebrew go to https://brew.sh/ and follow the installation instructions.

Install Bazelisk with Homebrew:
```bash
$ brew install bazelisk
```
Verify that Bazel is installed on your machine:
```bash
$ bazel --version
```
