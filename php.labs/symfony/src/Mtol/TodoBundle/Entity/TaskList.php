<?php

namespace Mtol\TodoBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * TaskList
 */
class TaskList
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
     * @var \Doctrine\Common\Collections\Collection
     */
    private $tasks;

    /**
     * Constructor
     */
    public function __construct()
    {
        $this->tasks = new \Doctrine\Common\Collections\ArrayCollection();
    }
    
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
     * @return TaskList
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
     * @return TaskList
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
     * @return TaskList
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
     * Add tasks
     *
     * @param \Mtol\TodoBundle\Entity\Task $tasks
     * @return TaskList
     */
    public function addTask(\Mtol\TodoBundle\Entity\Task $tasks)
    {
        $this->tasks[] = $tasks;
    
        return $this;
    }

    /**
     * Remove tasks
     *
     * @param \Mtol\TodoBundle\Entity\Task $tasks
     */
    public function removeTask(\Mtol\TodoBundle\Entity\Task $tasks)
    {
        $this->tasks->removeElement($tasks);
    }

    /**
     * Get tasks
     *
     * @return \Doctrine\Common\Collections\Collection 
     */
    public function getTasks()
    {
        return $this->tasks;
    }
}