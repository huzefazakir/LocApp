<?php
    require_once("./fb_php/src/facebook.php");
    
    $fbAppId = '355719041162891';
    $fbAppSecret = '49bf93902f8ce02c833f8e340ae5f8b0';
    $fbAccessToken = $_REQUEST['accessToken'];
    $visibilityState = $_REQUEST['visibility'];
    $result = null;
    #$fbAccessToken = 'BAAFDhksWnosBABhUK8sgy7LpFc8dZAJd82QzphZB0HajmknBKkX4HX3c8SjcyMHbKYX7YH6bOsNBDazWXjq6hhZCGZCFxlGJ92A78BX4nm8uN2iOp6r7ZC5bMQPSmiecO8DbgZBPg93wZDZD';
    #$visibilityState = 'visible';

    $conHandle = mysql_connect('127.0.0.1', 'root', 'location');

    if (!$conHandle) {
        die('Could not connect: ' . mysql_error());
    }

    $db_selected = mysql_select_db('user_db');

    if (!$db_selected) {
        die('Cannot use location: ' . mysql_error());
    }

    $config = array();
    $config['appId'] = $fbAppId;
    $config['secret'] = $fbAppSecret;

    $facebook = new Facebook($config);
    $facebook->setAccessToken($fbAccessToken);

    $fb_user_id = $facebook->getUser();
    if (!$fb_user_id) {
        $retVal = array('success' => 0);
        print(json_encode($retVal));
        exit;
    } else {
        try {
            $user_profile = $facebook->api('/me','GET');
            $fbUserName = $user_profile['name']; 
        } catch (FacebookApiException $e) {
            $retVal = array('success' => 0);
            print(json_encode($retVal));
            exit;
        }
        if ($visibilityState == "visible" || $visibilityState == "Visible") {
            $result = mysql_query("UPDATE user_location_table_v1 SET user_id ='".$fbUserName."' where fb_id=".$fb_user_id."" );
        } else if ($visibilityState == "invisible" || $visibilityState == "Invisible") {
            $result = mysql_query("UPDATE user_location_table_v1 SET user_id ='invisible' where fb_id=".$fb_user_id."" );
        }

        if($result) {
            if(mysql_affected_rows() == 1){
                $retVal = array('success' => 1);
            } else {
                $retVal = array('success' => 0);
            } 
        }  else {
            die('invalid update:'. mysql_error());            
        }
    }
    print(json_encode($retVal));
?>
    
    

