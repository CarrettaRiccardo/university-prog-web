<?php
$n = 1;
if (($fh = fopen('esami.txt', 'r')) && ($to = fopen('esami.sql', 'w')) ) {
    while (!feof($fh)) {
        $line = rtrim(fgets($fh));    
        fwrite($to, "INSERT INTO esami_prescrivibili (id,nome) VALUES ($n,'$line');\n"  );
        $n++;
    }
    fclose($fh);
}
else echo "Errrore 2<br/>";
echo "Finito";
?>