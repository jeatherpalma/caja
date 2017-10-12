package com.worknest.web.rest;

import com.worknest.CajaApp;

import com.worknest.domain.HistorialAcciones;
import com.worknest.repository.HistorialAccionesRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the HistorialAccionesResource REST controller.
 *
 * @see HistorialAccionesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CajaApp.class)
public class HistorialAccionesResourceIntTest {

    private static final String DEFAULT_NOMBRE_PERSONA = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_PERSONA = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE_CAJA = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_CAJA = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ACCION = "AAAAAAAAAA";
    private static final String UPDATED_ACCION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private HistorialAccionesRepository historialAccionesRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHistorialAccionesMockMvc;

    private HistorialAcciones historialAcciones;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HistorialAccionesResource historialAccionesResource = new HistorialAccionesResource(historialAccionesRepository);
        this.restHistorialAccionesMockMvc = MockMvcBuilders.standaloneSetup(historialAccionesResource)
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
    public static HistorialAcciones createEntity(EntityManager em) {
        HistorialAcciones historialAcciones = new HistorialAcciones()
            .nombrePersona(DEFAULT_NOMBRE_PERSONA)
            .nombreCaja(DEFAULT_NOMBRE_CAJA)
            .fecha(DEFAULT_FECHA)
            .accion(DEFAULT_ACCION)
            .descripcion(DEFAULT_DESCRIPCION);
        return historialAcciones;
    }

    @Before
    public void initTest() {
        historialAcciones = createEntity(em);
    }

    @Test
    @Transactional
    public void createHistorialAcciones() throws Exception {
        int databaseSizeBeforeCreate = historialAccionesRepository.findAll().size();

        // Create the HistorialAcciones
        restHistorialAccionesMockMvc.perform(post("/api/historial-acciones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historialAcciones)))
            .andExpect(status().isCreated());

        // Validate the HistorialAcciones in the database
        List<HistorialAcciones> historialAccionesList = historialAccionesRepository.findAll();
        assertThat(historialAccionesList).hasSize(databaseSizeBeforeCreate + 1);
        HistorialAcciones testHistorialAcciones = historialAccionesList.get(historialAccionesList.size() - 1);
        assertThat(testHistorialAcciones.getNombrePersona()).isEqualTo(DEFAULT_NOMBRE_PERSONA);
        assertThat(testHistorialAcciones.getNombreCaja()).isEqualTo(DEFAULT_NOMBRE_CAJA);
        assertThat(testHistorialAcciones.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testHistorialAcciones.getAccion()).isEqualTo(DEFAULT_ACCION);
        assertThat(testHistorialAcciones.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void createHistorialAccionesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = historialAccionesRepository.findAll().size();

        // Create the HistorialAcciones with an existing ID
        historialAcciones.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistorialAccionesMockMvc.perform(post("/api/historial-acciones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historialAcciones)))
            .andExpect(status().isBadRequest());

        // Validate the HistorialAcciones in the database
        List<HistorialAcciones> historialAccionesList = historialAccionesRepository.findAll();
        assertThat(historialAccionesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNombrePersonaIsRequired() throws Exception {
        int databaseSizeBeforeTest = historialAccionesRepository.findAll().size();
        // set the field null
        historialAcciones.setNombrePersona(null);

        // Create the HistorialAcciones, which fails.

        restHistorialAccionesMockMvc.perform(post("/api/historial-acciones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historialAcciones)))
            .andExpect(status().isBadRequest());

        List<HistorialAcciones> historialAccionesList = historialAccionesRepository.findAll();
        assertThat(historialAccionesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = historialAccionesRepository.findAll().size();
        // set the field null
        historialAcciones.setFecha(null);

        // Create the HistorialAcciones, which fails.

        restHistorialAccionesMockMvc.perform(post("/api/historial-acciones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historialAcciones)))
            .andExpect(status().isBadRequest());

        List<HistorialAcciones> historialAccionesList = historialAccionesRepository.findAll();
        assertThat(historialAccionesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccionIsRequired() throws Exception {
        int databaseSizeBeforeTest = historialAccionesRepository.findAll().size();
        // set the field null
        historialAcciones.setAccion(null);

        // Create the HistorialAcciones, which fails.

        restHistorialAccionesMockMvc.perform(post("/api/historial-acciones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historialAcciones)))
            .andExpect(status().isBadRequest());

        List<HistorialAcciones> historialAccionesList = historialAccionesRepository.findAll();
        assertThat(historialAccionesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHistorialAcciones() throws Exception {
        // Initialize the database
        historialAccionesRepository.saveAndFlush(historialAcciones);

        // Get all the historialAccionesList
        restHistorialAccionesMockMvc.perform(get("/api/historial-acciones?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historialAcciones.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombrePersona").value(hasItem(DEFAULT_NOMBRE_PERSONA.toString())))
            .andExpect(jsonPath("$.[*].nombreCaja").value(hasItem(DEFAULT_NOMBRE_CAJA.toString())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].accion").value(hasItem(DEFAULT_ACCION.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }

    @Test
    @Transactional
    public void getHistorialAcciones() throws Exception {
        // Initialize the database
        historialAccionesRepository.saveAndFlush(historialAcciones);

        // Get the historialAcciones
        restHistorialAccionesMockMvc.perform(get("/api/historial-acciones/{id}", historialAcciones.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(historialAcciones.getId().intValue()))
            .andExpect(jsonPath("$.nombrePersona").value(DEFAULT_NOMBRE_PERSONA.toString()))
            .andExpect(jsonPath("$.nombreCaja").value(DEFAULT_NOMBRE_CAJA.toString()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.accion").value(DEFAULT_ACCION.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHistorialAcciones() throws Exception {
        // Get the historialAcciones
        restHistorialAccionesMockMvc.perform(get("/api/historial-acciones/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHistorialAcciones() throws Exception {
        // Initialize the database
        historialAccionesRepository.saveAndFlush(historialAcciones);
        int databaseSizeBeforeUpdate = historialAccionesRepository.findAll().size();

        // Update the historialAcciones
        HistorialAcciones updatedHistorialAcciones = historialAccionesRepository.findOne(historialAcciones.getId());
        updatedHistorialAcciones
            .nombrePersona(UPDATED_NOMBRE_PERSONA)
            .nombreCaja(UPDATED_NOMBRE_CAJA)
            .fecha(UPDATED_FECHA)
            .accion(UPDATED_ACCION)
            .descripcion(UPDATED_DESCRIPCION);

        restHistorialAccionesMockMvc.perform(put("/api/historial-acciones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHistorialAcciones)))
            .andExpect(status().isOk());

        // Validate the HistorialAcciones in the database
        List<HistorialAcciones> historialAccionesList = historialAccionesRepository.findAll();
        assertThat(historialAccionesList).hasSize(databaseSizeBeforeUpdate);
        HistorialAcciones testHistorialAcciones = historialAccionesList.get(historialAccionesList.size() - 1);
        assertThat(testHistorialAcciones.getNombrePersona()).isEqualTo(UPDATED_NOMBRE_PERSONA);
        assertThat(testHistorialAcciones.getNombreCaja()).isEqualTo(UPDATED_NOMBRE_CAJA);
        assertThat(testHistorialAcciones.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testHistorialAcciones.getAccion()).isEqualTo(UPDATED_ACCION);
        assertThat(testHistorialAcciones.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void updateNonExistingHistorialAcciones() throws Exception {
        int databaseSizeBeforeUpdate = historialAccionesRepository.findAll().size();

        // Create the HistorialAcciones

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHistorialAccionesMockMvc.perform(put("/api/historial-acciones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historialAcciones)))
            .andExpect(status().isCreated());

        // Validate the HistorialAcciones in the database
        List<HistorialAcciones> historialAccionesList = historialAccionesRepository.findAll();
        assertThat(historialAccionesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHistorialAcciones() throws Exception {
        // Initialize the database
        historialAccionesRepository.saveAndFlush(historialAcciones);
        int databaseSizeBeforeDelete = historialAccionesRepository.findAll().size();

        // Get the historialAcciones
        restHistorialAccionesMockMvc.perform(delete("/api/historial-acciones/{id}", historialAcciones.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HistorialAcciones> historialAccionesList = historialAccionesRepository.findAll();
        assertThat(historialAccionesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistorialAcciones.class);
        HistorialAcciones historialAcciones1 = new HistorialAcciones();
        historialAcciones1.setId(1L);
        HistorialAcciones historialAcciones2 = new HistorialAcciones();
        historialAcciones2.setId(historialAcciones1.getId());
        assertThat(historialAcciones1).isEqualTo(historialAcciones2);
        historialAcciones2.setId(2L);
        assertThat(historialAcciones1).isNotEqualTo(historialAcciones2);
        historialAcciones1.setId(null);
        assertThat(historialAcciones1).isNotEqualTo(historialAcciones2);
    }
}
