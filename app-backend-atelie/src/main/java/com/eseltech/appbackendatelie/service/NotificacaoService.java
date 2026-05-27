package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.NotificacaoDTO;
import com.eseltech.appbackendatelie.entity.Empresa;
import com.eseltech.appbackendatelie.entity.Notificacao;
import com.eseltech.appbackendatelie.exceptions.ResourceNotFoundException;
import com.eseltech.appbackendatelie.repository.EmpresaRepository;
import com.eseltech.appbackendatelie.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    public List<Notificacao> findAll() {
        List<Notificacao> lista = notificacaoRepository.findAll();

        if (lista.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma notificação encontrada");
        }

        return lista;
    }

    public Notificacao findById(Integer id) {
        return notificacaoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notificação não encontrada com id: " + id));
    }

    public void removerNotificacao(Integer id) {
        notificacaoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notificação não encontrada com id: " + id));
        notificacaoRepository.deleteById(id);
    }

    @Transactional
    public Notificacao salvarNotificacao(Notificacao notificacaoEdit) {
        Empresa empresa = empresaRepository.findById(notificacaoEdit.getEmpresa().getId()).orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com id: " + notificacaoEdit.getEmpresa().getId()));

        Notificacao notificacao = new Notificacao();
        notificacao.setEmpresa(empresa);
        notificacao.setTopico(notificacaoEdit.getTopico());
        notificacao.setMensagem(notificacaoEdit.getMensagem());
        notificacao.setDtEnvio(notificacaoEdit.getDtEnvio());

        return notificacaoRepository.save(notificacao);
    }

    @Transactional
    public Notificacao atualizarNotificacao(Integer id, Notificacao notificacaoEdit) {
        Notificacao notificacao = notificacaoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notificação não encontrada com id: " + id));
        Empresa empresa = empresaRepository.findById(notificacaoEdit.getEmpresa().getId()).orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com id: " + notificacaoEdit.getEmpresa().getId()));

        notificacao.setEmpresa(empresa);
        notificacao.setTopico(notificacaoEdit.getTopico());
        notificacao.setMensagem(notificacaoEdit.getMensagem());
        notificacao.setDtEnvio(notificacaoEdit.getDtEnvio());

        return notificacaoRepository.save(notificacao);
    }

    public List<Notificacao> buscarNotificacoesAEnviar() {
        return notificacaoRepository.findNotificacoesAEnviar();
    }
}


