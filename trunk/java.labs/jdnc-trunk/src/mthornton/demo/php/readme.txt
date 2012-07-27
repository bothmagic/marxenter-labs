JNLP with PHP

To demonstrate the system place the files in this directory in the 
same directory as the .php files on a webserver supporting PHP (at least 
version 4). The links in WebStart.html should then work.

Note that jnlp.php manipulates href entries in the JNLP file so that any that appear to
be relative have "jnlp.php?file=" prepended to the reference (or "loggedjnlp.php?file=" if
the process was started via loggedphp.php). References to $$codebase and $$name are also
replaced (just as for the download servlet).
