<?php include("get_distance.php"); ?>

<?php
    require_once("./fb_php/src/facebook.php");
    $userLocation = $_REQUEST['location'];    
    $userLongitude = $_REQUEST['longitude'];
    $userLatitude = $_REQUEST['latitude'];
    $fbAccessToken = $_REQUEST['accessToken'];

    #$userLongitude = 52.7;
    #$userLatitude = 21.9;
    #$fbAccessToken = 'AAAFDhksWnosBAMVaZAdBBQwCZAvZCGSL4GZAJmhLQU7OZCUtPPQdZClBTMkoesCzCvc8MbU8LzrMepDO1schUYISv440gS6ZAcDx6lDs7w4MAZDZD';

    $fbAppId = '355719041162891';
    $fbAppSecret = '49bf93902f8ce02c833f8e340ae5f8b0';
    
    $config = array();
    $config['appId'] = $fbAppId;
    $config['secret'] = $fbAppSecret;

    $facebook = new Facebook($config);
    $facebook->setAccessToken($fbAccessToken);
    $userFBID = $facebook->getUser();
    
    if(!$userFBID){
        die('invalid access token');
    }
   
    $myDist = new DistanceCalculator($userLongitude, $userLatitude);
 
    $conHandle = mysql_connect('127.0.0.1', 'root', 'location');

    if (!$conHandle) {
	die('Could not connect: ' . mysql_error());
    }

    $db_selected = mysql_select_db('user_db');

    if (!$db_selected) {
        die('Cannot use location: ' . mysql_error());
    }
  
    mysql_query("UPDATE user_location_table_v1 SET longitude =".$userLongitude.", latitude =" .$userLatitude.", location='".$userLocation."' where fb_id =" .$userFBID."");
 
    $friends = $facebook->api('me/friends', 'GET', array('access_token' => $fbAccessToken) );
    $jLocation = array();

    for ($i = 0; $i<sizeof($friends['data']); $i++) {
        $friendsLocResult = mysql_query("SELECT * from user_location_table_v1 where fb_id=".$friends['data'][$i]['id']."");
        if(!$friendsLocResult) {
            echo "Invalid query";
        } else {
            if (mysql_num_rows($friendsLocResult) != 0) {
                $locRow = mysql_fetch_assoc($friendsLocResult);
                #if(!($locRow['latitude'] === null) && !($locRow['longitude'] === null)) {
                #	$distance =  $myDist->calculateDistance($locRow['latitude'], $locRow['longitude']);
                $distance = 0;
                $jLocation[]=array( "id" => $locRow['id'], "user_id" => $locRow['user_id'], "location" => $locRow['location'], "time_stamp" => $locRow['cur_timestamp'] );
                #}
            } 
        }
    }    
    print(json_encode($jLocation));
    mysql_close();
?>
