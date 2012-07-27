<?php
/*
 * $Id: jnlpVersions.php 941 2006-12-11 22:34:23Z mthornton $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *

*  Find a file matching a JNLP version specification.
*  Only the naming convention method of indicating version is supported. This 
*  is because PHP can easily obtain the list of available versions for a file 
*  by just using the glob function instead of having to read data for all files 
*  in a versions.xml. Now if there is a way to cache the result of parsing
*  versions.xml between requests ...

* Author: Mark Thornton (mthornton@optrak.co.uk)
* Date: 10 December 2006
*/

define(JNLP_VER_EXACT, 0);
define(JNLP_VER_PREFIX, 1);
define(JNLP_VER_MINIMUM, 2);

/* $filename --- name of resource requested
 * $version  --- version specification(s).
 * returns the actual filename if found or false.
 * If $version is passed by reference, it will be updated to the actual version
*/
function findVersionedFile($filename, $version)
{
    $pos = strrpos($filename, '.');
    if ($pos === false) {
        $basename = $filename;
        $ext = '';
    }
    else {
        $basename = substr($filename, 0, $pos);
        $ext = substr($filename, $pos);
    }
    // find names of all available versions of this resource
    $available = glob($basename.'__*'.$ext);
    if (count($available) == 0)
        return false;
    // now parse the version specification which may be a space separated list of versions
    $versions = parseVersionSpecification($version);
    $bestMatchIndex = count($versions)-1;
    foreach ($available as $file) {
        $fileVersion = parseFileVersion($file, $ext);
        $fv = $fileVersion[0];
        // compare against versions
        // TODO: check other requirements as well (OS, arch, locale), if present
        for ($index = 0; $index <= $bestMatchIndex; $index++) {
            $v = $versions[$index];
            if (matchVersion($fv, $v[1], $v[0])) {
                if (!isset($bestVersion) || ($index == $bestMatchIndex && compareVersion($fv, $bestVersion) > 0)) {
                    $bestMatchIndex = $index;
                    $bestVersion = $fv;
                    $result = $file;
                    break;
                }
            }
        }
    }
    if (!isset($result))
        return false;
    // extract actual version from filename
    $pos = strpos($result, '__V');
    $pos += 3;
    $end = strpos($result, '__', $pos);
    if ($end === false)
        $version = substr($result, $pos, -strlen($ext));
    else
        $version = substr($result, $pos, $end-$pos);
    return $result;
}

function matchVersion($version, $required, $type)
{
    switch ($type) {
        case JNLP_VER_EXACT:
            return matchExactVersion($version, $required);
        case JNLP_VER_MINIMUM:
            return compareVersion($version, $required) >= 0;
        case JNLP_VER_PREFIX:
            return matchPrefixVersion($version, $required);
    }
}

function padVersion($version, $target)
{
    $n = count(target);
    for ($i = count($version); $i < $n; $i++)
        $version[$i] = 0;
}

function matchExactVersion($version, $required)
{
    padVersion(&$version, &$required);
    padVersion(&$required, &$version);
    for ($i=0; $i < count($version); $i++) {
        if ($version[$i] !== $required[$i])
            return false;
    }
    return true;
}

function compareVersion($version, $required)
{
    padVersion(&$version, &$required);
    padVersion(&$required, &$version);
    for ($i=0; $i < count($version); $i++) {
        $a = $version[$i];
        $b = $required[$i];
        // if either is a string ensure that the other is also a string
        if (is_string($a))
            $b = (string)$b;
        else if (is_string($b))
            $a = (string)$a;
        if ($a > $b)
            return 1;
        if ($a < $b)
            return -1;
    }
    return 0;
}

function matchPrefixVersion($version, $required)
{
    // Note: only the required version is padded in this case
    padVersion(&$required, &$version);
    for ($i=0; $i < count($required); $i++) {
        if ($version[$i] !== $required[$i])
            return false;
    }
    return true;
}

function parseFileVersion($filename, $ext)
{
    $parts = explode('__', substr($filename, 0, -strlen($ext)));
    /* 
	option  ::= V version-id |
        	    O os |
            	    A arch |
            	    L locale
        The 'V' option must appear exactly once.
    */
    unset($parts[0]);   // remove the first part which is the base name of the file
    foreach ($parts as $opt) {
        $value = substr($opt, 1);
        $type = substr($opt, 0, 1);
        if ($type == 'V') {
            $result[0] = normaliseVersion($value);
        }
        else {
            $result[$type][] = $value;
        }
    }
    return $result;
}

function parseVersionSpecification($versions)
{
    // should probably use 'split' to permit multiple white space between versions
    $versions = explode(' ', $versions);
    foreach ($versions as &$ver) {
        // TODO: what do we do if the version contains an "&" character?
        // does the string end in '+' or '*'?
        switch (substr($ver, -1)) {
            case '+':
                $type = JNLP_VER_MINIMUM; break;
            case '*':
                $type = JNLP_VER_PREFIX; break;
            default:
                $type = JNLP_VER_EXACT; break;
        }
        if ($type != JNLP_VER_EXACT) {
            $ver = substr($ver, 0, -1);
        }
        // now split and normalise remaining version specification
        $ver = array($type, normaliseVersion($ver));
    }
    return $versions;
}

function normaliseVersion($ver) {
    // split into an array of components
    $ver = preg_split('/[._\-]/', $ver);
    // if any components are integer, replace the string with an integer
    // this ensures that '4' and '004' match
    foreach ($ver as &$component) {
        if (ctype_digit($component)) {
            $component = (int)$component;
        }
    }
    return $ver;
}
?>