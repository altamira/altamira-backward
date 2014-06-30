'use strict';

angular.module('webApp')
  .factory('PurchaseOrder', function ($q) {

    // Public API...
    return {
      getAll: function () {
        var deferred = $q.defer();

        deferred.resolve([
          { id : 62547,
            dataEmissao: '2014-03-28',
            faturamento : {
              fornecedor : {
                razaoSocial : 'ARMARINHOS FERNANDO LTDA',
                cnpj : '48076228002488',
                ie : '147106739110'
              },
              endereco : {
                logradouro : 'Rua Teste, 344',
                cep : '03167-070',
                municipio : 'SAO PAULO',
                uf : 'SP'
              },
              telefone : '95335130',
              contato : 'Teste',
              email : 'teste@teste.com'
            },
            condicaoPagamento : {

            },
            enderecoCobranca : {
                logradouro : 'Rua Teste Cobranca, 344',
                cep : '03167-070',
                municipio : 'SAO PAULO',
                uf : 'SP'
              },
            enderecoEntrega : {
                logradouro : 'Rua Teste Entrega, 1000',
                cep : '03167-072',
                municipio : 'SAO PAULO',
                uf : 'SP'
              },
            produtos : [ { arrivalDate : '16/12/2013',
                           thickness : 1.80,
                           width : 240,
                           length : 0,
                           lamination : 'FQ',
                           treatment : 'PR',
                           standard : 'ISO',
                           weight : 4.70,
                           ipi : 18.60,
                           unit : 1000.00,
                           total : 4700.00 },
                         { arrivalDate : '15/12/2013',
                           thickness : 1.80,
                           width : 240,
                           length : 0,
                           lamination : 'FQ',
                           treatment : 'PR',
                           standard : 'ISO',
                           weight : 4.70,
                           ipi : 18.60,
                           unit : 1000.00,
                           total : 4700.00 },
                         { arrivalDate : '14/12/2013',
                           thickness : 1.80,
                           width : 240,
                           length : 0,
                           lamination : 'FQ',
                           treatment : 'PR',
                           standard : 'ISO',
                           weight : 4.70,
                           ipi : 18.60,
                           unit : 1000.00,
                           total : 4700.00 } ],
            informacoesTecnicas : 'Espessura, Largura e Comprimento em Milimetros (mm), Quantidade em Toneladas (T)',
            observacoes : 'Observações de testes, colocadas para teste....',
            condicoesFornecimento : 'Relacionar na nota fiscal o numero do nosso pedido'
           }]);

        return deferred.promise;
      }
    };
  });
