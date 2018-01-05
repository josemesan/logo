package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LogoApp;

import com.mycompany.myapp.domain.Valor;
import com.mycompany.myapp.repository.ValorRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ValorResource REST controller.
 *
 * @see ValorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LogoApp.class)
public class ValorResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CAMPO = 1;
    private static final Integer UPDATED_CAMPO = 2;

    @Autowired
    private ValorRepository valorRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restValorMockMvc;

    private Valor valor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ValorResource valorResource = new ValorResource(valorRepository);
        this.restValorMockMvc = MockMvcBuilders.standaloneSetup(valorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Valor createEntity(EntityManager em) {
        Valor valor = new Valor()
            .nombre(DEFAULT_NOMBRE)
            .campo(DEFAULT_CAMPO);
        return valor;
    }

    @Before
    public void initTest() {
        valor = createEntity(em);
    }

    @Test
    @Transactional
    public void createValor() throws Exception {
        int databaseSizeBeforeCreate = valorRepository.findAll().size();

        // Create the Valor
        restValorMockMvc.perform(post("/api/valors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(valor)))
            .andExpect(status().isCreated());

        // Validate the Valor in the database
        List<Valor> valorList = valorRepository.findAll();
        assertThat(valorList).hasSize(databaseSizeBeforeCreate + 1);
        Valor testValor = valorList.get(valorList.size() - 1);
        assertThat(testValor.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testValor.getCampo()).isEqualTo(DEFAULT_CAMPO);
    }

    @Test
    @Transactional
    public void createValorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = valorRepository.findAll().size();

        // Create the Valor with an existing ID
        valor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restValorMockMvc.perform(post("/api/valors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(valor)))
            .andExpect(status().isBadRequest());

        // Validate the Valor in the database
        List<Valor> valorList = valorRepository.findAll();
        assertThat(valorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllValors() throws Exception {
        // Initialize the database
        valorRepository.saveAndFlush(valor);

        // Get all the valorList
        restValorMockMvc.perform(get("/api/valors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(valor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].campo").value(hasItem(DEFAULT_CAMPO)));
    }

    @Test
    @Transactional
    public void getValor() throws Exception {
        // Initialize the database
        valorRepository.saveAndFlush(valor);

        // Get the valor
        restValorMockMvc.perform(get("/api/valors/{id}", valor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(valor.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.campo").value(DEFAULT_CAMPO));
    }

    @Test
    @Transactional
    public void getNonExistingValor() throws Exception {
        // Get the valor
        restValorMockMvc.perform(get("/api/valors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateValor() throws Exception {
        // Initialize the database
        valorRepository.saveAndFlush(valor);
        int databaseSizeBeforeUpdate = valorRepository.findAll().size();

        // Update the valor
        Valor updatedValor = valorRepository.findOne(valor.getId());
        updatedValor
            .nombre(UPDATED_NOMBRE)
            .campo(UPDATED_CAMPO);

        restValorMockMvc.perform(put("/api/valors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedValor)))
            .andExpect(status().isOk());

        // Validate the Valor in the database
        List<Valor> valorList = valorRepository.findAll();
        assertThat(valorList).hasSize(databaseSizeBeforeUpdate);
        Valor testValor = valorList.get(valorList.size() - 1);
        assertThat(testValor.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testValor.getCampo()).isEqualTo(UPDATED_CAMPO);
    }

    @Test
    @Transactional
    public void updateNonExistingValor() throws Exception {
        int databaseSizeBeforeUpdate = valorRepository.findAll().size();

        // Create the Valor

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restValorMockMvc.perform(put("/api/valors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(valor)))
            .andExpect(status().isCreated());

        // Validate the Valor in the database
        List<Valor> valorList = valorRepository.findAll();
        assertThat(valorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteValor() throws Exception {
        // Initialize the database
        valorRepository.saveAndFlush(valor);
        int databaseSizeBeforeDelete = valorRepository.findAll().size();

        // Get the valor
        restValorMockMvc.perform(delete("/api/valors/{id}", valor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Valor> valorList = valorRepository.findAll();
        assertThat(valorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Valor.class);
        Valor valor1 = new Valor();
        valor1.setId(1L);
        Valor valor2 = new Valor();
        valor2.setId(valor1.getId());
        assertThat(valor1).isEqualTo(valor2);
        valor2.setId(2L);
        assertThat(valor1).isNotEqualTo(valor2);
        valor1.setId(null);
        assertThat(valor1).isNotEqualTo(valor2);
    }
}
