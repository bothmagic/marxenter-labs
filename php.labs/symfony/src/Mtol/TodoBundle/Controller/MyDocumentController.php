<?php
/**
 * Created by JetBrains PhpStorm.
 * User: markus
 * Date: 05.04.13
 * Time: 22:41
 * To change this template use File | Settings | File Templates.
 */

namespace Mtol\TodoBundle\Controller;


use Mtol\TodoBundle\Document\MyDocument;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Doctrine\ODM\PHPCR\Document\Folder;

class MyDocumentController extends Controller{

    function newAction() {
        // PHPCR session instance
        $session = $this->container->get('doctrine_phpcr.default_session');
        // PHPCR ODM document manager instance
        $documentManager = $this->container->get('doctrine_phpcr.odm.default_document_manager');



    }

    function getAction() {
        // PHPCR session instance
        $session = $this->container->get('doctrine_phpcr.default_session');
        // PHPCR ODM document manager instance
        $dm = $this->container->get('doctrine_phpcr.odm.default_document_manager');

        $root = $session->getRootNode();
        //$node = $root->addNode('test', 'nt:unstructured');
        //$node->setProperty('prop', 'value');
        //$session->save();

        //$node = $session->getNode('/test');

        $doc = new MyDocument();
        $doc->setParent($dm->find(null, '/test'));
        $doc->setNodename('mynode');
        $doc->setCount(1);

        /*$dm->persist($doc);
        $dm->flush();
        */


        return $doc;
    }

}