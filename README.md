# catBox
Modern approach to manipulate files with root permissions.
## Why command line tools is a bad approach?
The first problem is app developer may create su process in addition to the actual toybox/busybox process every time to execute privileged command.
Secondly, most apps use tools like toybox or busybox to read files.
For most cases there won't be any problems besides some minor argument differences between different versions of toybox/busybox.
Those tools may be unavailable on some weird systems though user may try to install them,
solution to this can be to bundle toybox binaries with app and extract them.
But there is may be a problem again. These binaries may not run on some devices (for unknown reason they just may give error "access denied" when running on su).
The next problem is that you need to process text which is slow and doesn't guarantee success because output may differ depending on binary.
## How catBox is different?
catBox uses binders to communicate between app and privileged process. It does not process text, does not require busybox or toybox.
Yet Android doesn't support exceptions across processes, so catBox won't throw any exceptions if something fails.
# Usage
To use catBox, import aar and initialize catBox by calling CatManager.init.\
You can use CatFile class just as you do with File to manipulate files, it contains some additional functions not available in the File class.
Yet not all functions from File are supported.\
CatPhone contains phone control related functions.\
You can control processes with CatProcess class.\
CatUser contains various information about current user.
