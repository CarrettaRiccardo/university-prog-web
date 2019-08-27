<?php
$n = 1;
if (($fh = fopen('visite.txt', 'r')) && ($to = fopen('visite_spec.sql', 'w')) ) {
    while (!feof($fh)) {
        $line = rtrim(fgets($fh));    
        fwrite($to, "INSERT INTO visite_specialistiche (id,nome) VALUES ($n,'$line');\n"  );
        $n++;
    }
    fclose($fh);
}
else echo "Errrore 2<br/>";
echo "Finito";
?>