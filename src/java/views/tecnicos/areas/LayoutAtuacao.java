/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views.tecnicos.areas;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import entidade.Atuacao;
import java.util.List;
import persistencia.AtuacaoDao;
import views.LayoutPrincipalTecnico;
import views.tecnicos.areas.LayoutCadastroAtuacao;

/**
 *
 * @author Marla Aragão
 */
public class LayoutAtuacao extends VerticalLayout {
    
    private Table tabela;
    private Button btnMenuPrincipal;
    private Button btnAlterar;
    private Button btnNovo;
    private TextField search;
    private VerticalLayout content;
    
    public LayoutAtuacao(VerticalLayout content) {
        
        this.content = content;
        
        setImmediate(true);
        setSpacing(true);
        setMargin(true);

        Label label = new Label("<font size=\"2\" color=\"#287ece\"><b>Atuação</b></font>"
        , ContentMode.HTML);
        
        addComponent(label);
        addComponent(new Label("<hr />", ContentMode.HTML));
        
        init();
        
    }
    
    private void init() {
        
        HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setImmediate(true);
        hLayout.setSpacing(true);
        
        btnMenuPrincipal = new Button("Menu Principal");
        btnMenuPrincipal.setImmediate(true);
        btnMenuPrincipal.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                LayoutPrincipalTecnico l = new LayoutPrincipalTecnico();
                UI.getCurrent().setContent(l);
            }
        });
        
        btnAlterar = new Button("Alterar");
        btnAlterar.setImmediate(true);
        btnAlterar.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Object rowId = tabela.getValue();
                if (rowId == null) {
                    Notification.show("", "Escolha uma área atuacao para alterar", Notification.Type.WARNING_MESSAGE);
                } else {
                    
                    Atuacao atuacao = (Atuacao) rowId;
                    
                    LayoutCadastroAtuacao l = new LayoutCadastroAtuacao(LayoutCadastroAtuacao.Operacao.ALTERAR, content);
                    l.loadDados(atuacao);
                    
                    content.removeAllComponents();
                    content.addComponent(l);
                }
            }
        });
        
        hLayout.addComponent(btnAlterar);
        
        btnNovo = new Button("Nova Atuacao");
        btnNovo.setImmediate(true);
        btnNovo.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                LayoutCadastroAtuacao l = new LayoutCadastroAtuacao(LayoutCadastroAtuacao.Operacao.INCLUIR, content);
                
                content.removeAllComponents();
                content.addComponent(l);
            }
        });
        
        hLayout.addComponent(btnNovo);
                
        search = new TextField();
        search.setImmediate(true);
        search.setWidth("100%");
        search.setInputPrompt("Pesquisa");
        search.addTextChangeListener(new FieldEvents.TextChangeListener() {

            Filter filtro = null;
            
            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                Container.Filterable f = (Container.Filterable) tabela.getContainerDataSource();
                
                if (filtro != null)
                    f.removeContainerFilter(filtro);
                
                filtro = new SimpleStringFilter("Descricao", event.getText(),
                                                true, false);
                f.addContainerFilter(filtro);
            }
        });
        

        tabela = buildTabela();
        
        this.addComponent(btnMenuPrincipal);
        this.addComponent(hLayout);
        this.addComponent(search);
        this.addComponent(tabela);
        
        carregarTabelaAtuacaos();
        
    }
    
    private Table buildTabela() {
        Table tabela_ = new Table();

        tabela_.setSelectable(true);
        tabela_.setImmediate(true);
        tabela_.setMultiSelect(false);
        tabela_.setSizeFull();
      
        tabela_.addContainerProperty("Id", Integer.class, null);
        tabela_.addContainerProperty("Descricao", String.class, null);
               
        tabela_.setColumnHeaders(new String[]{"Id", "Descrição"});
        tabela_.setColumnWidth("Codigo", 60);

        tabela_.setVisibleColumns(new Object[]{"Id", "Descricao"});
    
        return tabela_;
    }
    
    private void carregarTabelaAtuacaos() {
        tabela.removeAllItems();     
        try {
            
            List<Atuacao> listaAtuacaos = null;

            listaAtuacaos = new AtuacaoDao().selectAll();
            
            for (Atuacao p : listaAtuacaos) {
                Item item = tabela.addItem(p);
                item.getItemProperty("Id").setValue(p.getId());
                item.getItemProperty("Descricao").setValue(p.getDescricao());
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
