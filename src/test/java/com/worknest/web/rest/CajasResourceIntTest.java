package com.worknest.web.rest;

import com.worknest.CajaApp;

import com.worknest.domain.Cajas;
import com.worknest.repository.CajasRepository;
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

import com.worknest.domain.enumeration.Status;
/**
 * Test class for the CajasResource REST controller.
 *
 * @see CajasResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CajaApp.class)
public class CajasResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Status DEFAULT_ESTATUS = Status.LIBRE;
    private static final Status UPDATED_ESTATUS = Status.ASIGNADA;

    private static final Long DEFAULT_SUCURSAL = 1L;
    private static final Long UPDATED_SUCURSAL = 2L;

    @Autowired
    private CajasRepository cajasRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCajasMockMvc;

    private Cajas cajas;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CajasResource cajasResource = new CajasResource(cajasRepository);
        this.restCajasMockMvc = MockMvcBuilders.standaloneSetup(cajasResource)
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
    public static Cajas createEntity(EntityManager em) {
        Cajas cajas = new Cajas()
            .nombre(DEFAULT_NOMBRE)
            .estatus(DEFAULT_ESTATUS)
            .sucursal(DEFAULT_SUCURSAL);
        return cajas;
    }

    @Before
    public void initTest() {
        cajas = createEntity(em);
    }

    @Test
    @Transactional
    public void createCajas() throws Exception {
        int databaseSizeBeforeCreate = cajasRepository.findAll().size();

        // Create the Cajas
        restCajasMockMvc.perform(post("/api/cajas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cajas)))
            .andExpect(status().isCreated());

        // Validate the Cajas in the database
        List<Cajas> cajasList = cajasRepository.findAll();
        assertThat(cajasList).hasSize(databaseSizeBeforeCreate + 1);
        Cajas testCajas = cajasList.get(cajasList.size() - 1);
        assertThat(testCajas.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCajas.getEstatus()).isEqualTo(DEFAULT_ESTATUS);
        assertThat(testCajas.getSucursal()).isEqualTo(DEFAULT_SUCURSAL);
    }

    @Test
    @Transactional
    public void createCajasWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cajasRepository.findAll().size();

        // Create the Cajas with an existing ID
        cajas.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCajasMockMvc.perform(post("/api/cajas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cajas)))
            .andExpect(status().isBadRequest());

        // Validate the Cajas in the database
        List<Cajas> cajasList = cajasRepository.findAll();
        assertThat(cajasList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = cajasRepository.findAll().size();
        // set the field null
        cajas.setNombre(null);

        // Create the Cajas, which fails.

        restCajasMockMvc.perform(post("/api/cajas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cajas)))
            .andExpect(status().isBadRequest());

        List<Cajas> cajasList = cajasRepository.findAll();
        assertThat(cajasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEstatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = cajasRepository.findAll().size();
        // set the field null
        cajas.setEstatus(null);

        // Create the Cajas, which fails.

        restCajasMockMvc.perform(post("/api/cajas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cajas)))
            .andExpect(status().isBadRequest());

        List<Cajas> cajasList = cajasRepository.findAll();
        assertThat(cajasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSucursalIsRequired() throws Exception {
        int databaseSizeBeforeTest = cajasRepository.findAll().size();
        // set the field null
        cajas.setSucursal(null);

        // Create the Cajas, which fails.

        restCajasMockMvc.perform(post("/api/cajas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cajas)))
            .andExpect(status().isBadRequest());

        List<Cajas> cajasList = cajasRepository.findAll();
        assertThat(cajasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCajas() throws Exception {
        // Initialize the database
        cajasRepository.saveAndFlush(cajas);

        // Get all the cajasList
        restCajasMockMvc.perform(get("/api/cajas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cajas.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].estatus").value(hasItem(DEFAULT_ESTATUS.toString())))
            .andExpect(jsonPath("$.[*].sucursal").value(hasItem(DEFAULT_SUCURSAL.intValue())));
    }

    @Test
    @Transactional
    public void getCajas() throws Exception {
        // Initialize the database
        cajasRepository.saveAndFlush(cajas);

        // Get the cajas
        restCajasMockMvc.perform(get("/api/cajas/{id}", cajas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cajas.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.estatus").value(DEFAULT_ESTATUS.toString()))
            .andExpect(jsonPath("$.sucursal").value(DEFAULT_SUCURSAL.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCajas() throws Exception {
        // Get the cajas
        restCajasMockMvc.perform(get("/api/cajas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCajas() throws Exception {
        // Initialize the database
        cajasRepository.saveAndFlush(cajas);
        int databaseSizeBeforeUpdate = cajasRepository.findAll().size();

        // Update the cajas
        Cajas updatedCajas = cajasRepository.findOne(cajas.getId());
        updatedCajas
            .nombre(UPDATED_NOMBRE)
            .estatus(UPDATED_ESTATUS)
            .sucursal(UPDATED_SUCURSAL);

        restCajasMockMvc.perform(put("/api/cajas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCajas)))
            .andExpect(status().isOk());

        // Validate the Cajas in the database
        List<Cajas> cajasList = cajasRepository.findAll();
        assertThat(cajasList).hasSize(databaseSizeBeforeUpdate);
        Cajas testCajas = cajasList.get(cajasList.size() - 1);
        assertThat(testCajas.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCajas.getEstatus()).isEqualTo(UPDATED_ESTATUS);
        assertThat(testCajas.getSucursal()).isEqualTo(UPDATED_SUCURSAL);
    }

    @Test
    @Transactional
    public void updateNonExistingCajas() throws Exception {
        int databaseSizeBeforeUpdate = cajasRepository.findAll().size();

        // Create the Cajas

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCajasMockMvc.perform(put("/api/cajas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cajas)))
            .andExpect(status().isCreated());

        // Validate the Cajas in the database
        List<Cajas> cajasList = cajasRepository.findAll();
        assertThat(cajasList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCajas() throws Exception {
        // Initialize the database
        cajasRepository.saveAndFlush(cajas);
        int databaseSizeBeforeDelete = cajasRepository.findAll().size();

        // Get the cajas
        restCajasMockMvc.perform(delete("/api/cajas/{id}", cajas.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Cajas> cajasList = cajasRepository.findAll();
        assertThat(cajasList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cajas.class);
        Cajas cajas1 = new Cajas();
        cajas1.setId(1L);
        Cajas cajas2 = new Cajas();
        cajas2.setId(cajas1.getId());
        assertThat(cajas1).isEqualTo(cajas2);
        cajas2.setId(2L);
        assertThat(cajas1).isNotEqualTo(cajas2);
        cajas1.setId(null);
        assertThat(cajas1).isNotEqualTo(cajas2);
    }
}
