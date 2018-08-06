<?php
	include('config.php');
	$content = file_get_contents("php://input"); //get all data sent
	$update = json_decode($content, true);  //decode dat juicy jsonObject
	
	
	//no update , no need to continue
	if(!$update)
	{
	  exit;
	}
	
	$pawnId = isset($update['pawnId']) ? $update['pawnId'] : "";
	$playerId = isset($update['playerId']) ? $update['playerId'] : "";
	$player2Id = isset($update['player2Id']) ? $update['player2Id'] : "";
	$op = isset($update['op']) ? $update['op'] : "";
	$pos = isset($update['pos']) ? $update['pos'] : "";
	$gameId = isset($update['gameId']) ? $update['gameId'] : "";
	$valToUpdate = isset($update['valToUpdate']) ? $update['valToUpdate'] : "";
	
	//CREATE implementation
	$tableName = isset($update['tableName']) ? $update['tableName'] : "";
	$val1 = isset($update['val1']) ? $update['val1'] : "";
	$val2 = isset($update['val2']) ? $update['val2'] : "";

	
	
	
	//CREATE IF
	if($op == "CREATE") // if checking what kind of operation i'm requesting
	{		
	
		if($tableName=="player"){
				//check if player already exists
				$sql="SELECT COUNT(Name) as UNIVIDEO FROM Users WHERE Name like '" . mysqli_real_escape_string($conn, $NameApp ) . "'";
				$result=$conn->query($sql);
				while($row = $result->fetch_assoc()){
				$nameFound=$row['UNIVIDEO'];
													}
				//end of the chekerin
				if($nameFound==0){ //if it's a new name then proceed with the registration
				$passCreate=$val1 . $val2;
				$passCreate=md5($passCreate);
				$sql="INSERT INTO Users (Name,Password,Overall_Score,Win,Lost) VALUES ('" . mysql_real_escape_string($conn, $val1) . "' , '" . $passCreate . "',0,0,0)";
				$result=$conn->query($sql);}
				
				$parameters = array("RegisterStatus" => $nameFound); // buildin the Response JSON     , 0 means the registration was a success , !=0 means it was not.			
		}
		
		
		
	}
	
	
	if($op == "READ") {
		if($tableName=="game"){//sending all pawn' positions // meglio fare solo 1 query. Select * from GAME where gameId = idGame
		                                                    // poi si fa semplicemente il parse e si smistano i record
			$parameters = array("gameId" => $gameId);
			$sql="SELECT * FROM Pawn WHERE id_Player like " . $playerId . " AND id_Game like " . $gameId ;
			$result=$conn->query($sql);
				while($row = $result->fetch_assoc()){//Player1's Pawn
				$parameters['playerId'][$playerId]['pawnId'][$row['ID']]['playerId'][]=$row['id_Player'];//2 volte player Id?
				$parameters['playerId'][$playerId]['pawnId'][$row['ID']]['position'][]=$row['Position'];
													}	
														
				$sql="SELECT * FROM Pawn WHERE id_Player like " . $player2Id . " AND id_Game like " . $gameId ;
				$result=$conn->query($sql);
				while($row = $result->fetch_assoc()){//Player2's Pawn
				$parameters['playerId'][$playerId]['pawnId'][$row['ID']]['playerId'][]=$row['id_Player'];
				$parameters['playerId'][$playerId]['pawnId'][$row['ID']]['position'][]=$row['Position'];
													}	
													
		}
		
		
		if($tableName=="pawn" && $playerId!=NULL){ //if i do have the player id then i've understood the app wants the amount of pawns in the sent pawn $pos
												//!!REMEMBER TO SEND $POS , IF NOT ,LET ME KNOW SINCE I HAVE TO DO ANOTHER QUERY (totally doable)
			$sql="SELECT COUNT(ID) AS EJA FROM Pawn WHERE ID_Player like '". $playerId . "' and id_Game like '". $gameId . "' and Position like " .$pos ; 	
		$result=$conn->query($sql);
		while($row = $result->fetch_assoc()){
		$NumPawn=$row['EJA'];
											}
		$parameters = array("gameId" => $gameId, "playerId" => $playerId , "position" => $NumPawn); // buildin the Response JSON
					
		}
			
		
		

	}

//response encoding

header('Content-Type: application/json');
echo json_encode($parameters, TRUE);


/*È UN VERO WALT DISNEY SOLO SE HA LA GARANZIA DI AUTENTICITÀ DELLA QUALITÀ DELL'OLOGRAMMA ARGENTATO 
ED IL MARCHIO UNIVIDEO PRESENTI SULLA CONFEZIONE.
 NON ACCETTATE I FALSI, ESIGETE SEMPRE E SOLO VIDEOCASSETTE ORIGINALI WALT DISNEY HOME VIDEO.*/
?>