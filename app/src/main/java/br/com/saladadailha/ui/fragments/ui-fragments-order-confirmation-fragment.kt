package br.com.saladadailha.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.saladadailha.databinding.FragmentOrderConfirmationBinding

class OrderConfirmationFragment : Fragment() {
    
    private var _binding: FragmentOrderConfirmationBinding? = null
    private val binding get() = _binding!!
    
    private val args: OrderConfirmationFragmentArgs by navArgs()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.txtOrderNumber.text = "Número do pedido: #${args.orderId}"
        
        binding.btnBackToMenu.setOnClickListener {
            findNavController().navigate(
                OrderConfirmationFragmentDirections.actionOrderConfirmationFragmentToMenuFragment()
            )
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
