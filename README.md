# jVersion Checker

#### Check whether the version of the android application on the user’s phone is up to date or not. If the version is old, a message will be shown warning the user to update the version and forcing him to update because he cannot close the message except when updating. If it is up to date, no warning messages will appear to the user.


## Installation

#### Step 1. Add the JitPack repository to your settings file (new build structure), or build file (old build structure):

```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
 ```

#### Step 2. Add the dependency

```
dependencies {
        implementation 'com.github.abanoub92:jVersionChecker:x.x.x'
        implementation 'com.google.android.play:app-update:2.1.0'
}
```
[![](https://jitpack.io/v/abanoub92/jVersionChecker.svg)](https://jitpack.io/#abanoub92/jVersionChecker)

## Usage

#### A sample usage of a sample and easy library

```
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VersionChecker checker = VersionChecker.getInstance(this);
        checker.check();
    }
}
```


## Library License

##### Library Licensed under the Apache License, see the [License](https://github.com/abanoub92/VersionChecker?tab=Apache-2.0-1-ov-file)
