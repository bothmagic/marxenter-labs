<?php

namespace Mtol\TodoBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Task
 *
 */
class Task
{
    /**
     * @var integer
     */
    private $id;

    /**
     * @var string
     */
    private $shortdescr;

    /**
     * @var string
     */
    private $longdescr;

    /**
     * @var boolean
     */
    private $state;

    /**
     * Get id
     *
     * @return integer 
     */
    public function getId()
    {
        return $this->id;
    }

    /**
     * Set shortdescr
     *
     * @param string $shortdescr
     * @return Task
     */
    public function setShortdescr($shortdescr)
    {
        $this->shortdescr = $shortdescr;
    
        return $this;
    }

    /**
     * Get shortdescr
     *
     * @return string 
     */
    public function getShortdescr()
    {
        return $this->shortdescr;
    }

    /**
     * Set longdescr
     *
     * @param string $longdescr
     * @return Task
     */
    public function setLongdescr($longdescr)
    {
        $this->longdescr = $longdescr;
    
        return $this;
    }

    /**
     * Get longdescr
     *
     * @return string 
     */
    public function getLongdescr()
    {
        return $this->longdescr;
    }

    /**
     * Set state
     *
     * @param boolean $state
     * @return Task
     */
    public function setState($state)
    {
        $this->state = $state;
    
        return $this;
    }

    /**
     * Get state
     *
     * @return boolean 
     */
    public function getState()
    {
        return $this->state;
    }
    /**
     * @var \Mtol\TodoBundle\Entity\TaskList
     */
    private $product;


    /**
     * Set product
     *
     * @param \Mtol\TodoBundle\Entity\TaskList $product
     * @return Task
     */
    public function setProduct(\Mtol\TodoBundle\Entity\TaskList $product = null)
    {
        $this->product = $product;
    
        return $this;
    }

    /**
     * Get product
     *
     * @return \Mtol\TodoBundle\Entity\TaskList 
     */
    public function getProduct()
    {
        return $this->product;
    }
}