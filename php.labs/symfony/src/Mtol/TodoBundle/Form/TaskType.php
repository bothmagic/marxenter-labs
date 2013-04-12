<?php

namespace Mtol\TodoBundle\Form;

use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolverInterface;

class TaskType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $builder
            ->add('shortdescr')
            ->add('longdescr')
            ->add('state')
        ;
    }

    public function setDefaultOptions(OptionsResolverInterface $resolver)
    {
        $resolver->setDefaults(array(
            'data_class' => 'Mtol\TodoBundle\Entity\Task',
            'csrf_protection' => false
        ));
    }

    public function getName()
    {
        return 'task';
    }
}
