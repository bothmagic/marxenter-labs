<?php
/*
 * $Id: jnlp.php 944 2006-12-12 20:04:22Z mthornton $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.

 * PHP Implementation of Java Network Launch Protocol.
 * This implementation supports both basic and versioned downloads, but 
 * JARDIFF support is not provided.

 * Usage:
 * http://myserver.org/webstart/jnlp.php?file=somecode.jar
 * 
 * The use of a query parameter allows a single PHP script to be used for all
 * resources. The parameter is a relative path to the resource. For security 
 * reasons, absolute paths and ".." are not permitted in the file parameter.
 * This means you can't use it to obtain files from elsewhere on the server.

 * When WebStart requests a versioned resource, the query ends up looking something like
 * jnlp.php?file=somecode.jar?version-id=1.2
 * That is WebStart's developers hadn't expected the base path to already be a query!
 * Never mind, I just split off this bit which comes in mixed with the file parameter.

 * Author: Mark Thornton (mthornton@optrak.co.uk)
 * Date: 10 December 2006
*/
$method = $_SERVER['REQUEST_METHOD'];
if (isset($log))
    $text = $_SERVER['REMOTE_ADDR'].','.microtime().','.$method.','.$_SERVER['QUERY_STRING'];
$filename = $_GET['file'];
// Check for mangled query (extra '?')
$pos = strpos($filename, '?');
if ($pos != false) {
    $x = substr($filename, $pos+1);
    $filename = substr($filename, 0, $pos);
    $_GET['file'] = $filename;
    $pos = strpos($x, '=');
    if ($pos !== false) {
        $key = substr($x, 0, $pos);
        $value = substr($x, $pos+1);
        $_GET[$key] = $value;
    }
}
// check that file is in a subdirectory of here (for security reaons)
$filename = str_replace('\\', '/', $filename);
$pos = strpos($filename, '/');
if ($pos !== false) {
    if (@pos == 0 || strpos($filename, '../') === 0 || strpos($filename, '/../') !== false) {
	header('HTTP/1.0 403 Forbidden');
        if (isset($log)) {
            fwrite($log, "$text 403 Forbidden: $filename\r\n");
            fclose($log);
        }
        exit;
    }
}
// If a version is specified, find the actual filename
if (isset($_GET['version-id'])) {
    $version = $_GET['version-id'];
    // determine actual filename and version
    include 'jnlpVersions.php';
    $filename = findVersionedFile($filename, &$version);
    if (isset($log))
        $text = $text.',version='.$version;
}
if ($filename === false || !file_exists($filename)) {
	header('HTTP/1.0 404 File not found');
        $filename = $_GET['file'];
        if (isset($log)) {
            fwrite($log, $text." 404 -- File not found: '$filename'\r\n");
            fclose($log);
        }
	exit;
}
$file_extension = strtolower(substr(strrchr($filename,"."),1));
// Determine appropriate MIME type based on the extension
switch ($file_extension) {
case 'jar':
    // TODO: check for jardiff possibility
    // TODO: check for Pack200 and gzip options here
    $ctype='Content-type: application/x-java-archive'; 
    break;
case 'jnlp':
    $ctype='Content-type: application/x-java-jnlp-file'; 
    break;
case 'jpg':
case 'jpeg': $ctype='Content-type: image/jpeg'; break;
case 'gif':  $ctype='Content-type: image/gif'; break;
default:
        // Only allow types relevant to JNLP (security precaution)
	header('HTTP/1.0 403 Forbidden');
        if (isset($log)) {
            fwrite($log, "$text 403 Forbidden type: $file_extension\r\n");
            fclose($log);
        }
	exit;
}
// Format modified date/time in preferred RFC1123 style.
$moddate = date('D, j M Y H:i:s O', filemtime($filename));
if (isset($version)) {
    header('x-java-jnlp-version-id: '.$version);
}
// Checking if the client is validating his cache and if it is current.
if (isset($_SERVER['HTTP_IF_MODIFIED_SINCE']) && (strtotime($_SERVER['HTTP_IF_MODIFIED_SINCE']) == filemtime($filename))) {
   // Client's cache IS current, so we just respond '304 Not Modified'.
    header('Last-Modified: '.$moddate, true, 304);
    header($ctype);
    if (isset($log))
        fwrite($log, $text." 304 -- No change since: ".$_SERVER['HTTP_IF_MODIFIED_SINCE']."\r\n");
} else {
    // file out of date or not cached
    header('Last-Modified: '.$moddate, true, 200);
    header($ctype);
    if ($method == 'GET') {
        $buffer = ''; 
        $cnt =0; 
        $handle = fopen($filename, 'rb');
        if ($file_extension == 'jnlp') {
            // process jnlp file line by line
            if (isset($log))
                $jnlp = 'loggedjnlp.php?file=';
            else
                $jnlp = 'jnlp.php?file=';
            $server = 'http://'.$_SERVER['HTTP_HOST'];
            $codebase = $_SERVER['REQUEST_URI'];
            $pos = strrpos($codebase, '=');
            if ($pos !== false)
                $name = substr($codebase, $pos+1);
            $pos = strrpos($codebase, '/');
            if ($pos === false)
                $codebase = $server.'/';
            else
                $codebase = $server.substr($codebase, 0, $pos+1);
            while (!feof($handle)) {
                $line = fgets($handle);
                $pos = 0;
                while (true) {
                    $pos = strpos($line, 'href=', $pos);
                    if ($pos === false)
                        break;
                    $pos += 6;
                    if (substr_compare($line, 'http:', $pos, 5) != 0) {
                        $head = substr($line, 0, $pos).$jnlp;
                        $line = $head.substr($line, $pos);
                        $pos = strlen($head);
                    }
                }
                $line = str_replace('$$codebase', $codebase, $line);
                if (isset($name))
                    $line = str_replace('$$name', $name, $line);
                $cnt += strlen($line);
                print $line;
            }
        } else {
            $chunksize = 1*(20*1024); // how many bytes per chunk 
            while (!feof($handle)) { 
                $buffer = fread($handle, $chunksize); 
                echo $buffer; 
                ob_flush(); 
                flush(); 
                $cnt += strlen($buffer); 
            } 
        }
        $status = fclose($handle); 
        if (isset($log))
            fwrite($log, $text.' written '.$cnt." bytes, ".microtime()."\r\n");
    } else if (isset($log)) {
        // PHP would automatically terminate the script after headers were complete for a HEAD request
        // but in that case we wouldn't get the chance to write our log and cleanup.
        fwrite($log, $text." ".$moddate."\r\n");
    }
}
if (isset($log))
    fclose($log);
exit;
?>