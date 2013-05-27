Eurelis - OpenCms Admin Module
==============================

This module developed by [Eurelis][eurelis] is intended to add several tools to Administrators.


![System Information icon][system-information] System Information
-----------------------------------------------------------------
The aim of the **System Information** tool is to check the state of the system with graphs and statistics.
The graphs rely on [highcharts][highcharts].
This tool can be accessed through the **Administration** view of [OpenCms][opencms].

### ![Overview][overview] Overview
This section offers the Administrator to:
- check the current operating system version
- check the current java version
- see the current JVM uptime and start time
- set the refresh interval for the graphs (default: 5000 ms)

### ![CPU & Threads & Classes][cpu-threads] CPU, Threads, Classes
This section offers the Administrator to:
- See the number of used processors
- Check the CPU usage
- Check the Heap space (max, size and used memory)
- Count the loaded and total loaded classes
- Count the Live and Deamon threads

### ![Memory][memory] Memory
This section offers the Administrator to check the max, total and used memories:
- Perm Gen memory
- Old Gen memory
- Edan Space memory
- Survivor memory

### ![Database Pools][database] Database Pools
This section offers the Administrator to see all the database pools used:
- Usage graph
- URL
- Pool strategy
- Max pool size
- Active and idle connections



![File Information icon][file-information] File Information
------------------



[eurelis]: http://www.eurelis.com "Agitateur de Technologies"
[opencms]: http://www.opencms.org/ "OpenCms"
[highcharts]: http://www.highcharts.com/ 'Highcharts JS"

[system-information]: /vfs_module/system/workplace/resources/tools/admin/icons/big/system-information.png "System Information"
[file-information]: /vfs_module/system/workplace/resources/tools/admin/icons/big/file-information.png "File Information"
[overview]: /vfs_module/system/workplace/resources/tools/admin/icons/small/overview.png "Overview"
[cpu-threads]: /vfs_module/system/workplace/resources/tools/admin/icons/small/cpu-threads.png "CPU & Threads & Classes"
[memory]: /vfs_module/system/workplace/resources/tools/admin/icons/small/memory.png "Memory"
[database]: /vfs_module/system/workplace/resources/tools/admin/icons/small/database.png "Database Pools"