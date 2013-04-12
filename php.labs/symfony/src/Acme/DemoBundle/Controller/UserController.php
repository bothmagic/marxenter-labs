<?php

namespace Acme\DemoBundle\Controller;
use FOS\RestBundle\Controller\FOSRestController;
use Acme\StoreBundle\Entity\Product;
use FOS\RestBundle\Controller\Annotations as Rest;
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;

class UserController extends FOSRestController
{
    /**
     * @Rest\View
     */
    public function allAction()
    {
        $em = $this->getDoctrine()->getManager();
        $entities = $em->getRepository('AcmeStoreBundle:Product')->findAll();
        
        return array('entities' => $entities);
    }

    /**
     * @Rest\View
     */
    public function getAction($id)
    {
       $em = $this->getDoctrine()->getManager();
        $entities = $em->getRepository('AcmeStoreBundle:Product')->findById($id);

        return array('user' => $entities);
    }
}

?>