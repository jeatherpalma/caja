package com.worknest.web.rest;

import com.worknest.CajaApp;

import com.worknest.domain.Cajeros;
import com.worknest.repository.CajerosRepository;
import com.worknest.web.rest.errors.ExceptionTranslator;

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
 * Test class for the CajerosResource REST controller.
 *
 * @see CajerosResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CajaApp.class)
public class CajerosResourceIntTest {

    private static final Long DEFAULT_ID_CAJERO = 1L;
    private static final Long UPDATED_ID_CAJERO = 2L;

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDOS = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDOS = "BBBBBBBBBB";

    private static final Float DEFAULT_FONDO = 50F;
    private static final Float UPDATED_FONDO = 49F;

    @Autowired
    private CajerosRepository cajerosRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCajerosMockMvc;

    private Cajeros cajeros;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CajerosResource cajerosResource = new CajerosResource(cajerosRepository);
        this.restCajerosMockMvc = MockMvcBuilders.standaloneSetup(cajerosResource)
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
    public static Cajeros createEntity(EntityManager em) {
        Cajeros cajeros = new Cajeros()
            .idCajero(DEFAULT_ID_CAJERO)
            .nombre(DEFAULT_NOMBRE)
            .apellidos(DEFAULT_APELLIDOS)
            .fondo(DEFAULT_FONDO);
        return cajeros;
    }

    @Before
    public void initTest() {
        cajeros = createEntity(em);
    }

    @Test
    @Transactional
    public void createCajeros() throws Exception {
        int databaseSizeBeforeCreate = cajerosRepository.findAll().size();

        // Create the Cajeros
        restCajerosMockMvc.perform(post("/api/cajeros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cajeros)))
            .andExpect(status().isCreated());

        // Validate the Cajeros in the database
        List<Cajeros> cajerosList = cajerosRepository.findAll();
        assertThat(cajerosList).hasSize(databaseSizeBeforeCreate + 1);
        Cajeros testCajeros = cajerosList.get(cajerosList.size() - 1);
        assertThat(testCajeros.getIdCajero()).isEqualTo(DEFAULT_ID_CAJERO);
        assertThat(testCajeros.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCajeros.getApellidos()).isEqualTo(DEFAULT_APELLIDOS);
        assertThat(testCajeros.getFondo()).isEqualTo(DEFAULT_FONDO);
    }

    @Test
    @Transactional
    public void createCajerosWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cajerosRepository.findAll().size();

        // Create the Cajeros with an existing ID
        cajeros.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCajerosMockMvc.perform(post("/api/cajeros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cajeros)))
            .andExpect(status().isBadRequest());

        // Validate the Cajeros in the database
        List<Cajeros> cajerosList = cajerosRepository.findAll();
        assertThat(cajerosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkIdCajeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = cajerosRepository.findAll().size();
        // set the field null
        cajeros.setIdCajero(null);

        // Create the Cajeros, which fails.

        restCajerosMockMvc.perform(post("/api/cajeros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cajeros)))
            .andExpect(status().isBadRequest());

        List<Cajeros> cajerosList = cajerosRepository.findAll();
        assertThat(cajerosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = cajerosRepository.findAll().size();
        // set the field null
        cajeros.setNombre(null);

        // Create the Cajeros, which fails.

        restCajerosMockMvc.perform(post("/api/cajeros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cajeros)))
            .andExpect(status().isBadRequest());

        List<Cajeros> cajerosList = cajerosRepository.findAll();
        assertThat(cajerosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkApellidosIsRequired() throws Exception {
        int databaseSizeBeforeTest = cajerosRepository.findAll().size();
        // set the field null
        cajeros.setApellidos(null);

        // Create the Cajeros, which fails.

        restCajerosMockMvc.perform(post("/api/cajeros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cajeros)))
            .andExpect(status().isBadRequest());

        List<Cajeros> cajerosList = cajerosRepository.findAll();
        assertThat(cajerosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCajeros() throws Exception {
        // Initialize the database
        cajerosRepository.saveAndFlush(cajeros);

        // Get all the cajerosList
        restCajerosMockMvc.perform(get("/api/cajeros?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cajeros.getId().intValue())))
            .andExpect(jsonPath("$.[*].idCajero").value(hasItem(DEFAULT_ID_CAJERO.intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS.toString())))
            .andExpect(jsonPath("$.[*].fondo").value(hasItem(DEFAULT_FONDO.doubleValue())));
    }

    @Test
    @Transactional
    public void getCajeros() throws Exception {
        // Initialize the database
        cajerosRepository.saveAndFlush(cajeros);

        // Get the cajeros
        restCajerosMockMvc.perform(get("/api/cajeros/{id}", cajeros.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cajeros.getId().intValue()))
            .andExpect(jsonPath("$.idCajero").value(DEFAULT_ID_CAJERO.intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.apellidos").value(DEFAULT_APELLIDOS.toString()))
            .andExpect(jsonPath("$.fondo").value(DEFAULT_FONDO.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCajeros() throws Exception {
        // Get the cajeros
        restCajerosMockMvc.perform(get("/api/cajeros/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCajeros() throws Exception {
        // Initialize the database
        cajerosRepository.saveAndFlush(cajeros);
        int databaseSizeBeforeUpdate = cajerosRepository.findAll().size();

        // Update the cajeros
        Cajeros updatedCajeros = cajerosRepository.findOne(cajeros.getId());
        updatedCajeros
            .idCajero(UPDATED_ID_CAJERO)
            .nombre(UPDATED_NOMBRE)
            .apellidos(UPDATED_APELLIDOS)
            .fondo(UPDATED_FONDO);

        restCajerosMockMvc.perform(put("/api/cajeros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCajeros)))
            .andExpect(status().isOk());

        // Validate the Cajeros in the database
        List<Cajeros> cajerosList = cajerosRepository.findAll();
        assertThat(cajerosList).hasSize(databaseSizeBeforeUpdate);
        Cajeros testCajeros = cajerosList.get(cajerosList.size() - 1);
        assertThat(testCajeros.getIdCajero()).isEqualTo(UPDATED_ID_CAJERO);
        assertThat(testCajeros.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCajeros.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testCajeros.getFondo()).isEqualTo(UPDATED_FONDO);
    }

    @Test
    @Transactional
    public void updateNonExistingCajeros() throws Exception {
        int databaseSizeBeforeUpdate = cajerosRepository.findAll().size();

        // Create the Cajeros

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCajerosMockMvc.perform(put("/api/cajeros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cajeros)))
            .andExpect(status().isCreated());

        // Validate the Cajeros in the database
        List<Cajeros> cajerosList = cajerosRepository.findAll();
        assertThat(cajerosList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCajeros() throws Exception {
        // Initialize the database
        cajerosRepository.saveAndFlush(cajeros);
        int databaseSizeBeforeDelete = cajerosRepository.findAll().size();

        // Get the cajeros
        restCajerosMockMvc.perform(delete("/api/cajeros/{id}", cajeros.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Cajeros> cajerosList = cajerosRepository.findAll();
        assertThat(cajerosList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cajeros.class);
        Cajeros cajeros1 = new Cajeros();
        cajeros1.setId(1L);
        Cajeros cajeros2 = new Cajeros();
        cajeros2.setId(cajeros1.getId());
        assertThat(cajeros1).isEqualTo(cajeros2);
        cajeros2.setId(2L);
        assertThat(cajeros1).isNotEqualTo(cajeros2);
        cajeros1.setId(null);
        assertThat(cajeros1).isNotEqualTo(cajeros2);
    }
}
