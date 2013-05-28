# Acme Air Sample and Benchmark

This application shows an implementation of a fictitious airline called "Acme Air".  The application was built with the some key business requirements: the ability to scale to billions of web API calls per day, the need to develop and deploy the application in public clouds (as opposed to dedicated pre-allocated infrastructure), and the need to support multiple channels for user interaction (with mobile enablement first and browser/Web 2.0 second).

There are two implementations of the application
- ** NodeJS to Mongodb **
- ** Java / WebSphere Liberty Profile to WebSphere eXtreme Scale **

## Repository Contents

Source:

- **acmeair-common**: The Java entities used throughout the application
- **acmeair-loader**:  A tool to load the Java implementation data store
- **acmeair-services**:  The Java data services interface definitions
- **acmeair-service-wxs**:  A WebSphere eXtreme Scale data service implementation
- **acmeair-webapp**:  The Web 2.0 application and associated Java REST services
- **acmeair-webapp-nodejs**: A implementation of the Acme Air application in NodeJS with a MongoDB data store backend

## Ask Questions

Questions about the Acme Air Open Source Project can be directed to our Google Groups.

* Acme Air Users: [https://groups.google.com/forum/?fromgroups#!forum/acmeair-users](https://groups.google.com/forum/?fromgroups#!forum/acmeair-users)

## Submit a bug report

We use github issues to report and handle bug reports.

## OSS Contributions

We accepts contributions via pull requests.

We will be posting important information about CLA agreements needed for us to accept pull requests soon.