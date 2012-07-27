#!/usr/bin/perl

my $fh;
open($fh,"find . -name \"*.java\" -print |") or die;
while (my $f=<$fh>) {
  chomp $f;
  print "$f\n";

  open my $ffh,"<$f" or die;
  open my $fout,">$f.1" or die;

  my $package=0;
  while(my $line=<$ffh>) {
    #print $line;
    if ($line=~/^package/) {
      $package=1;
      my $msg=<<EOF;
/* ******************************************************************************
 *
 *       Copyright 2008-2010 Hans Oesterholt-Dijkema
 *       This file is part of the JDesktop SwingX library
 *       and part of the SwingLabs project
 *
 *   SwingX is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as 
 *   published by the Free Software Foundation, either version 3 of 
 *   the License, or (at your option) any later version.
 *
 *   SwingX is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with SwingX.  If not, see <http://www.gnu.org/licenses/>.
 *   
 * ******************************************************************************/
EOF
      print $fout $msg;
      print $fout "\n";
      print $fout $line;
    } elsif ($package==1) {
      print $fout $line;
    }
  }
  close $fout;
  close $ffh;

  unlink($f);
  rename("$f.1",$f);
}

