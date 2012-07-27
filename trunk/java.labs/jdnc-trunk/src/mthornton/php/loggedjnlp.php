<?php
/*
 * $Id: loggedjnlp.php 941 2006-12-11 22:34:23Z mthornton $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * Download JNLP resources with logging
 * This should also cause a JNLP file to be edited such that 'jnlp.php' is replaced by 'loggedjnlp.php'
 * Author: Mark Thornton
 * Date: 11 December 2006
*/
$log = fopen('jnlp.log', 'a');
include 'jnlp.php';
?>