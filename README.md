# ALM 

This Sales/Trader application was built in about 8 hours as a POC for a potential client in the middle east.  This was to replace email interactions between sales and trading for the purpose of price requests.  The system includes full authorisation for differing user profiles (sales and trading), includes pop up notifications on screens, reporting capability and an analytics tab to enable interrogation of hit rates by client, trader, product etc.

The system current covers FX and Bonds, but can be easily expanded to cover other products as needed.

The ALM application is an example application that is designed to show a series of functional capability including:
- Trade booking
- Trade version management
- Ingestion of data via a csv data pipeline
- Ingestion of data via a reoccurring RestAPI call
- Ingestion of data via Kafka topic subscription
- Use of views to join tables and provide derived fields
- Consolidation of data across multiple asset classes to provide a single position ladder showing aggregate cash movements by date and currency
- Front end features including charting
- Use of PBC’s including Notifications and Reporting


The instructions for building this application (from scratch) can also be found in the learning area of the Genesis website and it is provided for users of the sandboxes to help familiarise developers with the Genesis platform and its capabilities.


This project has been created from the Genesis Blank Application Seed. Our seeds allow users to quickly bootstrap
their projects. Each seed adheres to strict Genesis best practices, and has passed numerous performance, compliance and
accessibility checks. 

Asset Liability Management

# Introduction

## Next Steps

To get a simple application running check the [Quick Start](https://learn.genesis.global/docs/getting-started/quick-start/) guide.

If you need an introduction to the Genesis platform and its modules it's worth heading [here](https://learn.genesis.global/docs/getting-started/learn-the-basics/simple-introduction/).


## Project Structure

This project contains **server** and **client** directories which contain the server and client code respectively.

### Server

The server code for this project can be found [here](./server/README.md).
It is built using a DSL-like definition based on the Kotlin language: GPAL.

When first opening the project, if you receive a notification from IntelliJ IDE detecting Gradle project select the option to 'Load as gradle project'.

### Web Client

The Web client for this project can be found [here](./client/README.md). It is built using Genesis's next
generation web development framework, which is based on Web Components.

# License

This is free and unencumbered software released into the public domain. For full terms, see [LICENSE](./LICENSE)

**NOTE** This project uses licensed components listed in the next section, thus licenses for those components are required during development.

## Licensed components
Genesis low-code platform
