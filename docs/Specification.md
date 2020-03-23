# Specifikáció

## Reversi
A reversi egy kétszemélyes táblajáték amelyet 8x8-as táblán játszanak, 64 db
két különböző oldalú koronggal.  Több variánsa is létezik a játéknak, a
feladatban a leggyakrabban alkalmazott szabályrendszert valósítjuk meg
(Othello).

### Szabályok
A táblára az egyik játékos *világos*, míg a másik *sötét* korongokat helyez el
felváltva. 

Egy lépés akkor érvényes, ha a soron következő játékos korongja
  - egy üres mezőre kerül
  - egy másik saját koronggal közrefogja az ellenfél korongját/korongjait
    (ezt megteheti vízsintesen, függőlegesen és átlósan is)

Egy érvényes lépés után az összes közrefogott korong megfordul, így színt vált.
Amennyiben a játékosnak nincs lehetősége érvényes lépésre, passzolnia kell, így
ismét az ellenfél lép. Viszont ha van lehetőség érvényes lépésre, akkor tilos
passzolni.

### A játék célja
A cél, hogy a korongok többségének színe a játékosé legyen.

### A játék kezdete
A táblán a játék kezdetén a középső négy rácsra átlósan el kell helyezni 2-2
sötét és világos korongot. A sötét korongok ÉK-DNY, míg a világos korongok
ÉNY-DK irányúak.

### A játék vége
Ha megtelik a tábla, vagy ha egyik játékos sem tud érvényes lépést tenni, azaz
ha egymás után mindkét játékos passzol. Ilyenkor meg kell számolni a táblán
lévő korongokat szín szerint. A több koronggal rendelkező játékos nyer.

## Az alkalmazás funciói
Az alkalmazás több extra funkcióval is rendelkezik, ezzel növelve a
játékélményt.

### Egy- és többjátékos mód
A felhasználó képes a saját számítógépén AI ellen játékot indítani, emellett
helyi hálózaton képes egy másik személy ellen játszani. A hálózati játék
megkezdése előtt meg kell adni egy felhasználónév és IP cím kombinációt, amely
segítségével kapcsolódhat a két kliens.

### Többszintű néhézségi szint
A felhasználó az egyszemélyes játékmódban több nehézségi szinten tud a
számítógép ellen játszani. Emellett többjátékos módban lehetőség van maximális
lépésre fordítható idő beállítására, ezzel is finomhangolva a nehézségi
szintet.

### Mentés és betöltés
Egyjátékos módban lehetőség van a játékállás elmentésére és későbbi
betöltésére.  Többjátékos módban a végeredmények elmentésére és ezek
megjelenítésére.

## Felhasználói felület
A játék indítása és a beállításai is mind grafikus felületen történnek.

### Főmenü
A játék indításakor a főmenü jelenik meg a felhasználó előtt. Itt van
lehetőség kiválasztani a játékmódot (gép ellen, helyi kétszemélyes
vagy hálózaton keresztüli). A főmenü tartalmazza a beállításokat is,
amik az éppen kiválasztott mód alapján válnak aktívvá.

A lehetséges beállítások:
- Rendelkezésre álló idő (minden mód esetén)
- Egyszemélyes mód esetén
  + Nehézségi szint választása (könnyű, normál, nehéz)
  + A játékos színének kiválasztása (világos, sötét)
- Hálózati játék esetén
  + Név
  + Ellenfél IP címének megadása
  + Saját szín kiválasztása

Kétszemélyes játék esetén a rendelkezésre álló idő beállításán kívül
nincs lehetőség másra. A játékmód, nehézségi szint és szín választása
rádiógombokkal lehetséges, míg az idő, név és IP cím megadására
szövegdobozok.

### Játék közben

A játék megkezdésekor megjelenik a tábla, az egyes korongok a táblán
lévő mezőkre kattintással helyezhetők el. Kiemelésre kerülnek azon
mezők, amelyekre érvényes lépés tehető. Amennyiben a játékosnak nincs
lehetősége érvényes lépésre egy üzenetet kap erről, majd kis idő múlva
ismét az ellenfél következik.

Hálózati játék esetén olyan játékos kaphat meghívást játékra, aki a
főmenüben tartózkodik. Ilyenkor egy felugró ablak értesíti a
kihívásról, amit akkor fogadhat el, ha érvényes felhasználónév van
beállítva.

A meccs feladására, illetve hálózati játék esetén döntetlen
felkínálására szolgáló gombok is találhatók ezen a felületen.
