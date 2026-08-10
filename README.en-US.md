# What is this project for
- Provides a monolith Spring Boot scaffold for rapid development. It often takes a lot of time to set up common functionalities such as package structure, logging, log format files, and database configuration when creating a new project. After cloning and modifying the configuration information, the basic framework is established and ready to use.
- For specific dependencies, please refer to `pom.xml`
- Integrates commonly used Spring Boot related dependencies, the best scaffold for writing monolithic projects

## Special Notes

1. All annotations with `Enable` that can be used in the project are already added to the startup class
2. All business code is written in the domain package

## Package Introduction

### application module
```text
└───com
    └───aks
        └───scaffold
            ├───aspectj interface log
            ├───config configuration
            ├───constant constants
            ├───controller
            │   ├───exception global exception interceptor
            ├───domain
            │   └───common
            │       ├───entity basic entity classes
            │       ├───mapper basic mapper
            │       └───service basic service
            ├───filter
            └───toolkit
                ├───page pagination
                └───redis redis utility class
```
### sdk module
```text
└───com
    └───aks
        └───sdk
            ├───exception custom exceptions
            ├───model common models
            ├───resp response models
            └───util 
                ├───asserts assertion tools
                ├───file file-related utilities
                ├───jwt jwt utility class
                ├───md5 md5 utility class
                └───thread thread utilities
```
