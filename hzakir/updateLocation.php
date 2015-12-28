<?php
    require_once("./fb_php/src/facebook.php");

    $fbAppId = '355719041162891';
    $fbAppSecret = '49bf93902f8ce02c833f8e340ae5f8b0';
    $fbAccessToken = $_REQUEST['accessToken'];
    $location = $_REQUEST['location'];
    #$location ='Alaska';
    #$fbAccessToken = 'AAAFDhksWnosBAMVaZAdBBQwCZAvZCGSL4GZAJmhLQU7OZCUtPPQdZClBTMkoesCzCvc8MbU8LzrMepDO1schUYISv440gS6ZAcDx6lDs7w4MAZDZD';

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

    $user_profile = $facebook->api('/me','GET');

    if(!$fb_user_id) {
        $retVal = array('success' => 0);
        print(json_encode($retVal));
        exit;
    } else {
        $result = mysql_query("SELECT fb_id from user_location_table_v1 where fb_id=".$fb_user_id."");
        if (mysql_num_rows($result) == 0) {
            #echo "inserting new user";
            mysql_query ("INSERT INTO user_location_table_v1 (fb_id, user_id, location) values (".$fb_user_id.",'".$user_profile['name']."','".$location."')");
	} else {
            mysql_query("UPDATE user_location_table_v1 SET location='".$location."' where fb_id =".$fb_user_id."");
        }

        mysql_close();
    }

    $retVal = array('success' => 1);
    print(json_encode($retVal));
?>
           



