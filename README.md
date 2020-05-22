# pep2-hf
A beágyazott rendszerek szoftvertechnológiája tárgy beadandó házifeladata

## Futtatás
java --module-path ${JAVAFX_LIB_ÚTVONAL} --add-modules
javafx.controls,javafx.fxml,javafx.graphics,javafx.web -jar Reversi.jar


## Környezet beállítása
### JavaFx
SDK letöltése innen: https://gluonhq.com/products/javafx/

Valahova csomagold ki, IntelliJ-ben \
File/Project Structure/Global libraries/New global library/Java \
Add hozzá a kicsomagolt mappából a lib/*.jar file-okat.

A GUI fejlesztéséhez ajánlott a Scene Builder program használata \
(https://gluonhq.com/products/scene-builder/)
