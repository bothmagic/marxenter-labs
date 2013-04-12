<?php

namespace Mtol\TodoBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Route;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Template;
use FOS\RestBundle\Controller\Annotations as Rest;
use Mtol\TodoBundle\Entity\Task;
use Mtol\TodoBundle\Form\TaskType;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;

class TodoController extends Controller
{
    /**
    * Collection get action
    * @var Request $request
    * @return array
    *
    * @Rest\View()
    */
   public function allAction()
   {
       $em = $this->getDoctrine()->getManager();
       $entities = $em->getRepository('MtolTodoBundle:Task')->findAll();
       return $entities;
   }
   
   /**
     * @Rest\View
     */
    public function getAction($id)
    {
        $em = $this->getDoctrine()->getManager();
       $entities = $em->getRepository('MtolTodoBundle:Task')->findById($id);
       return $entities[0];
    }
    
    /**
     * @Rest\View
     */
    public function newAction()
    {
        $logger = $this->get('logger');
        $logger->info('newAction');
        return $this->processForm(new Task());
    }
    
    private function processForm(Task $task)
    {
        $logger = $this->get('logger');
        $em = $this->getDoctrine()->getManager();
        $statusCode = $task->getId() < 0 ? 201 : 204;
        $logger->info("1");
        try {
            
        $form = $this->createForm(new TaskType(), $task);
        
        
        $logger->info("2 ".$this->getRequest());
        $request = $this->getRequest();
        $form->bind($request);
        $logger->info($this->getRequest()->attributes->count());
        if ($form->isValid()) {
            
            $em->persist($task);
            $em->flush();

            $response = new Response();
            $response->setStatusCode($statusCode);
            $response->headers->set('Location',
                $this->generateUrl(
                    'mtoltodo_get', array('id' => $task->getId()),
                    true // absolute
                )
            );
            $logger->info('done');
            return $response;
        }
        } catch (Exception $e) {
            $logger->info($e->getMessage());
        }


        return View::create($form, 400);
    }
}
