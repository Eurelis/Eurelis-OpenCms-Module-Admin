![Eurelis][eurelis-icon-big]urelis - OpenCms Module Admin
==============================

This module developed by [Eurelis][eurelis] is intended to add several tools to Administrators.


![System Information icon][system-information] System Information
-----------------------------------------------------------------
The aim of the **System Information** tool is to check the state of the system with graphs and statistics.
The graphs rely on [highcharts][highcharts].

### Usage:
This tool can be accessed through the **Administration** view of [OpenCms][opencms].

### ![Overview][overview] Overview:
This section offers the Administrator to:
- check the current operating system version
- check the current java version
- see the current JVM uptime and start time
- set the refresh interval for the graphs (default: 5000 ms)

### ![CPU & Threads & Classes][cpu-threads] CPU, Threads, Classes:
This section offers the Administrator to:
- See the number of used processors
- Check the CPU usage
- Check the Heap space (max, size and used memory)
- Count the loaded and total loaded classes
- Count the Live and Deamon threads

### ![Memory][memory] Memory:
This section offers the Administrator to check the max, total and used memories:
- Perm Gen memory
- Old Gen memory
- Edan Space memory
- Survivor memory

### ![Database Pools][database] Database Pools:
This section offers the Administrator to see all the database pools used:
- Usage graph
- URL
- Pool strategy
- Max pool size
- Active and idle connections



![File Information icon][file-information] File Information
-----------------------------------------------------------
The aim of the **File Information** tool is to search for files on [OpenCms][opencms] basing on:
- a root folder *(default: `/`)*
- a maximum and/or minimum file size
- the creation date of the file *(created before and/or after given dates)*

Then, the list of files appear and can be deleted and published.

### Usage:
This tool can be accessed:
- either through the **Administration** view of [OpenCms][opencms]
- or by right clicking on a folder on the **Explorer**  view of [OpenCms][opencms]



![XML Transformation icon][xml-transform-big] XML Transformation tool:
-----------------------------------------------------------------
The aim of the **XML Transformation** tool is to transform massively XML contents which corresponding XSD has changed with a new version of a module.

### Usage:
This tool can be used in the **Explorer**  view of [OpenCms][opencms] by:
- Creating a new ![XML Transformation icon][xml-transform] **XML Transformation description** file (accessible through the  ![Eurelis tools][eurelis-icon] **urelis functionalities page**)
- Edit the created file by defining the transformation to apply (see [Editing an XML Transformation description file][xml-transfo-desc])
- Right-click on the file and choose `Process transformation`: the document or folder containing XML documents to transform will be asked.

### Editing an XML Transformation description file:
- Write the Content Type as it is displayed in the `Type` column in the **Explorer**  view of [OpenCms][opencms]
- Add the Unitary Transformations according to your needs
  1. Write the XPath source to identify your node or group of nodes to transform (move, delete, update)
  2. Choose a Destination to define the target place to insert the source node(s)
  3. Choose a Template with defined parameters to define the target result


[eurelis]: http://www.eurelis.com "Agitateur de Technologies"
[opencms]: http://www.opencms.org/ "OpenCms"
[highcharts]: http://www.highcharts.com/ 'Highcharts JS"
[xml-transfo-desc]: #editing-an-xml-transformation-description-file "Editing an XML Transformation description file"

[system-information]: /vfs_module/system/workplace/resources/tools/admin/icons/big/system-information.png "System Information"
[file-information]: /vfs_module/system/workplace/resources/tools/admin/icons/big/file-information.png "File Information"
[overview]: /vfs_module/system/workplace/resources/tools/admin/icons/small/overview.png "Overview"
[cpu-threads]: /vfs_module/system/workplace/resources/tools/admin/icons/small/cpu-threads.png "CPU & Threads & Classes"
[memory]: /vfs_module/system/workplace/resources/tools/admin/icons/small/memory.png "Memory"
[database]: /vfs_module/system/workplace/resources/tools/admin/icons/small/database.png "Database Pools"
[eurelis-icon-big]: /vfs_module/system/workplace/resources/filetypes/eurelis_big.png "Eurelis Tools"
[eurelis-icon]: /vfs_module/system/workplace/resources/filetypes/eurelis.png "Eurelis Tools"
[xml-transform-big]: /vfs_module/system/workplace/resources/filetypes/xml-transform_big.png "XML Transformation tool"
[xml-transform]: /vfs_module/system/workplace/resources/filetypes/xml-transform.png "XML Transformation tool"

