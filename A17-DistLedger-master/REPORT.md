Nesta fase da entrega baseamo-nos no livro indicado na cadeira.
O gossip é feito pelo administrador, o servidor chamado pelo admin utiliza um GossipUtils.java, criado, que facilita a execucção de algumas das operações do gossip, tal como comparaçao e atualização(merge) de timestamps.

No createAccount do servidor nao utilizamos o TS mas sim o account existente nas operações de criação de conta existentes lista de operações já executadas indexada pelo nome da conta.

Para verificar que uma operaçao esta contida em um ledger usamos a comparaçao r.ts <= replicaTS, como indicada no livro, juntamente com  funções que verificam se o conteúdo da operação pedida se encontra em alguma das listas criadas para o registo destas.