# Examples
<h3 align="center"> Программа для голосования "Voting"</h3>
<p align="justify">
&nbsp;&nbsp;&nbsp;&nbsp;Пример простой программы. За основу взят процесс голосования. <br>Имеется 3 реализации программы: <ul>
<li> прямое взаимодействие Java приложение - SQL;
<li> Java client - Java server - SQL;
<li> Java RMIclient - Java RMIserver - SQL;
</ul>
</p>
<p align="justify">
&nbsp;&nbsp;&nbsp;&nbsp;Избиратели должны зарегистрироваться в системе. 
Администратор заполняет список кандидатов. Каждый участник по сети или с того же самого компьютера входит в систему и голосует. 
Данные о проголосовавших накапливаются в базе данных. Дважды проголосовать нельзя. 
По окончании периода голосования система выдает результат (5 кандидатов с наибольшим колвом голосов).
</p>
<dl>
<dt>При запуске RMI версии изпользовать VM Arguments:
  <dd> для клиента "-Djava.security.policy=client.policy"
  <dd> для сервера "-Djava.security.policy=server.policy"
  <dd> в случае ошибки при запуске "-Djava.security.policy=no.policy"
</dl>

