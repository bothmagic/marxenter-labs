<?php
/**
 * Created by JetBrains PhpStorm.
 * User: markus
 * Date: 05.04.13
 * Time: 22:24
 * To change this template use File | Settings | File Templates.
 */

namespace Mtol\TodoBundle\Document;

use Doctrine\ODM\PHPCR\Mapping\Annotations as PHPCRODM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Bundle\PHPCRBundle\Validator\Constraints as Assert;
use JMS\Serializer\Annotation\Exclude;

/** @PHPCRODM\Document
 *  @Assert\ValidPhpcrOdm
 */
class MyDocument {


    /** @PHPCRODM\Long */
    private $count;
    /** @PHPCRODM\String */
    private $name; // type defaults to string

    /** @PHPCRODM\Parentdocument
      * @Exclude
      */
    private $parent;
    /** @PHPCRODM\Nodename */
    private $nodename;

    public function setCount($count)
    {
        $this->count = $count;
    }

    public function getCount()
    {
        return $this->count;
    }

    public function setName($name)
    {
        $this->name = $name;
    }

    public function getName()
    {
        return $this->name;
    }

    public function setNodename($nodename)
    {
        $this->nodename = $nodename;
    }

    public function getNodename()
    {
        return $this->nodename;
    }

    public function setParent($parent)
    {
        $this->parent = $parent;
    }

    public function getParent()
    {
        return $this->parent;
    }
}