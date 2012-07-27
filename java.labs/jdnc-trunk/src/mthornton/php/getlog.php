<?php
/*
 * $Id: getlog.php 941 2006-12-11 22:34:23Z mthornton $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * Obtain recent log lines relevant to current client.
    The final 1024 bytes of the log file is scanned for lines relating to the 
    same IP address as the current client.

    Author: Mark Thornton
    Date: 10 December 2006
*/
$filename='jnlp.log';
$handle = @fopen($filename, 'rb');
if ($handle === false) {
	header('HTTP/1.0 404 File not found');
} else {
    $prefix = $_SERVER['REMOTE_ADDR'].',';
    $n = strlen($prefix);
    header('Content-type: text/plain', true, 200);
    fseek($handle, -1024, SEEK_END);
    while (!feof($handle)) {
        $line = fgets($handle);
        if (strlen($line) > $n && strncmp($line, $prefix, $n) == 0) {
            print $line;
        }
    }
    fclose($handle);
}
?>