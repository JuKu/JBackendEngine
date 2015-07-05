# JBackendEngine
JBackendEngine is a flexible, full customizable and easy to use backend engine for your needs.
You can replace every manager or module.

The databases Hazelcast (Hazelcast.org), Cassandra and MySQL are supported by default (You have to put the modules jar file into the modules directory, so the backend engine can find them).

## Features
 - Full Module System (you can package modules and load them at runtime to extend the backend engine)
 - Fully configurable
 - Configuration Management (local and distributed(1))
 - Session Store
 - User Management(1)
 - flexible Manager Architecture
 - global Task Manager to execute tasks asynchronous, schedule tasks every period (for example every 5 minutes)(2)
 - execute Tasks asynchronous
 - NotificationService (modules or your classes can communicate together or you can use this service to exchange messages around the server cluster with Hazelcast(1))
 - UniqueUserIDGeneration (currenty only with Hazelcast by default, but you can add support for this feature with your own module)
 - Service Support (In Android they are like background services, which are running in the background, this backend engine also adds this option to run services in the background and access or manage them)
 
 (1) for this feature you need a module, which adds support for this option
 (2) Task Manager is using 2 Executor Service for this. You can change corePoolSize and maxPoolSize in the configuration files.
