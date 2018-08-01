<?php






$numero=123456;
$parole="ESKERE";
$parameters = array('test1' => $numero, "test2String" => $parole);
header('Content-Type: application/json');
echo json_encode($parameters, TRUE);
?>
